javapersonalization(personalizationObjects, doublequote) ::= "
<\n>

<personalizationObjects:{it|
echo 'Importing <it.filename>'

java oracle.jrad.tools.xml.importer.XMLImporter $lCURRENTDIR/EBS/java/personalization/<it.folder> -rootdir . -username apps -password $AppsPWD -dbconnection <doublequote>(description = (address_list = (address = (community = tcp.world)(protocol =tcp)(host = $lHOST)(port = $lPORT)))(connect_data = (sid=$lSID)))<doublequote>
<\n>}>"
