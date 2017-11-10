dbheader(projectName, objects) ::= "
set define off
set serveroutput on
prompt Guardium Raporlama Baslat..
select 'GuardAppEvent:Start','GuardAppEventType:PROJECT','GuardAppEventStrValue:<projectName>' from dual;
<dbobject(objects)>
<dbfooter()>"