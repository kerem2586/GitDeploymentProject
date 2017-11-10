javaobject(xmlObjects, doublequote) ::= "
<\n>

<xmlObjects:{it|
echo 'Importing <it.filename>'

java oracle.jrad.tools.xml.importer.XMLImporter $JAVA_TOP/<it.folder> -username apps -password $AppsPWD -rootdir $JAVA_TOP -dbconnection <doublequote>(description = (address_list = (address = (community = tcp.world)(protocol =tcp)(host = $lHOST)(port = $lPORT)))(connect_data = (sid=$lSID)))<doublequote>
<\n>}>"
