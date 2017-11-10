fndloadheader(ldtObjectsUs, ldtObjectsTr) ::= "
{

AppsPWD=$1

# American_America.AL32UTF8 sikinti yasandi

XNLS=$NLS_LANG
NLS_LANG= American_America.WE8ISO8859P9
export NLS_LANG
<fndobjectus(ldtObjectsUs)>
<fndobjecttr(ldtObjectsTr)>
<fndloadfooter()>
"