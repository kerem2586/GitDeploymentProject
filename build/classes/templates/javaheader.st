javaheader(javaObjects, xmlObjects, personalizationObjects, localizationObjects, doublequote, jspObjects, htmlExists, mediaExists) ::= "
{

# use /usr/xpg4/bin/awk for solaris awk for unix

AppsPWD=$1
lSID='LIVE1'
lTNS=`tnsping $TWO_TASK | grep HOST`
lHOST=`echo $lTNS | /usr/xpg4/bin/awk -F'HOST=' '{print $2}' | awk -F')' '{print $1}'`
lPORT=`echo $lTNS | /usr/xpg4/bin/awk -F'PORT=' '{print $2}' | awk -F')' '{print $1}'`
lCURRENTDIR=`pwd`
export lCURRENTDIR

echo 'Copy JAVA_TOP Files...'

cp -R deployment/java/* $JAVA_TOP/
if [ <htmlExists> = 'true' ]; then
  echo 'Copy OA_HTML files...'
  cp -R deployment/OA_HTML/* $OA_HTML/
  
  echo 'Compiling Jsp Files...'
  cd $OA_HTML
  <jspObjects:{it|$FND_TOP/patch/115/bin/ojspCompile.pl --compile -s '<it.jspName>' -p 2<\n>}>
  cd $lCURRENTDIR
fi

if [ <mediaExists> = 'true' ]; then
  echo 'Copy OA_MEDIA files...'
  cp -R erp/OA_MEDIA/* $OA_MEDIA/
fi

echo 'Compiling Java Files...'

<javaObjects:{it|javac $JAVA_TOP/<it.folder>/*.java<\n>}>

echo 'Importing Pages And Regions...'<\n>
<javaobject(xmlObjects, doublequote)>
echo 'Importing Personalizations..'<\n>
<javapersonalization(personalizationObjects, doublequote)>
echo 'Importing Localizations..'<\n>
<javalocalization(localizationObjects, doublequote)>
<javafooter()>"