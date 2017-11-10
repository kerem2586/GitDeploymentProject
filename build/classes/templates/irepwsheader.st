irepwsheader(wsObjects) ::= "
{

AppsPWD=$1

lCURRENTDIR=`pwd`
export lCURRENTDIR

irepTempFolder=`whoami`
export irepTempFolder
mkdir /usr/tmp/irep_$irepTempFolder

echo 'Importing Integration Repository Files...'
<irepwsobject(wsObjects)>
<irepwsfooter()>
"