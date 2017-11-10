dbobject(objects) ::= "
<\n>
<objects:{it|
prompt Creating <it.objectName>.<it.objectType>..........

prompt Creating <it.objectName>.<it.objectType>
@@deployment/DB_OBJECTS/<it.objectType>/<it.objectName>.<it.objectType>
spool db_object_error.log append;
show errors;
spool off;
<\n>}>"