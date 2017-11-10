javapersonalization(personalizationObjects, doublequote) ::= "
<\n>

<personalizationObjects:{it|
echo 'Importing <it.filename>'

java oracle.jrad.tools.xml.importer.XMLImporter $lCURRENTDIR/erp/java/personalization/<it.folder> -username apps -password $AppsPWD -rootdir $lCURRENTDIR/EBS/java/personalization -dbconnection <doublequote>(description = (address_list = (address = (community = tcp.world)(protocol =tcp)(host = $lHOST)(port = $lPORT)))(connect_data = (sid=$lSID)))<doublequote>
<\n>}>"
