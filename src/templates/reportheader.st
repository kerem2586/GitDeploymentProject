reportheader(rdfObjects, reportTemplates, doublequote) ::= "
{

AppsPWD=$1

# use /usr/xpg4/bin/awk for solaris awk for unix

lSID=`echo $TWO_TASK`
lTNS=`tnsping $TWO_TASK | grep HOST`
lHOST=`echo $lTNS | /usr/xpg4/bin/awk -F'HOST=' '{print $2}' | awk -F')' '{print $1}'`
lPORT=`echo $lTNS | /usr/xpg4/bin/awk -F'PORT=' '{print $2}' | awk -F')' '{print $1}'`
lCURRENTDIR=`pwd`
export lCURRENTDIR
CurrentDate=`date '+%Y%m%d_%H%M%S'`
export CurrentDate

echo 'Deploying Reports...'

<rdfObjects:{it|
echo 'Backup <it.applicationName> <it.language> <it.rdfName>.rdf'
cp $<it.applicationTop>/reports/<it.language>/<it.rdfName>.rdf $<it.applicationTop>/reports/<it.language>/<it.rdfName>_$CurrentDate.rdf

echo 'Copy <it.language> <it.rdfName>.rdf'
cp erp/reports/rdf/<it.applicationName>/<it.language>/<it.rdfName>.rdf $<it.applicationTop>/reports/<it.language>/<it.rdfName>.rdf
<\n>}>

<reportobject(reportTemplates, doublequote)>
<reportfooter()>
"