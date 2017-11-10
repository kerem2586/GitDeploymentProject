wftheader(wftObjects) ::= "
{

AppsPWD=$1

lCURRENTDIR=`pwd`
export lCURRENTDIR
CurrentDate=`date '+%Y%m%d_%H%M%S'`
export CurrentDate
XNLS=$NLS_LANG

echo 'Deploying Workflows...'
<wftobject(wftObjects)>
<wftfooter()>
"