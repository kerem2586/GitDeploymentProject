pllheader(pllObjects) ::= "
{

AppsPWD=$1

lCURRENTDIR=`pwd`
export lCURRENTDIR
CurrentDate=`date '+%Y%m%d_%H%M%S'`
export CurrentDate

FORMS60_PATH=$APPL_TOP/au/11.5.0/resource:$APPL_TOP/au/11.5.0/forms/TR
export FORMS60_PATH

echo 'Deploying PLL...'
<pllobject(pllObjects)>
<pllfooter()>
"