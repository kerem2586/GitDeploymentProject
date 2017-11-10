javalocalization(localizationObjects, doublequote) ::= "
<\n>

<localizationObjects:{it|
echo 'Importing <it.filename>'

java oracle.jrad.tools.trans.imp.XLIFFImporter -username apps -password $AppsPWD -dbconnection <doublequote>(description = (address_list = (address = (community = tcp.world)(protocol =tcp)(host = $lHOST)(port = $lPORT)))(connect_data = (sid=$lSID)))<doublequote> $lCURRENTDIR/erp/java/localization/<it.folder>

<\n>}>"
