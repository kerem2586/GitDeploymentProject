reportobject(reportTemplates, doublequote) ::= "
<\n>
<reportTemplates:{it|
echo 'Importing <it.fileName> <it.fileType>'

java oracle.apps.xdo.oa.util.XDOLoader UPLOAD -DB_USERNAME apps -DB_PASSWORD $AppsPWD -JDBC_CONNECTION 	$lHOST:$lPORT:$lSID -LOB_TYPE <it.lobType> -LOB_CODE <it.fileName> -XDO_FILE_TYPE <it.fileType> -FILE_NAME erp/reports/<it.fileDirectory> -APPS_SHORT_NAME <it.applName> -NLS_LANG <it.nlsLang> -LANGUAGE <it.language> -TERRITORY <it.territory> -LOG_FILE <it.fileName>.log
<\n>}>"
