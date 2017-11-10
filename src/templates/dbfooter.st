dbfooter() ::= "
prompt Scriptler tamamlandi..

spool control.log
set define off;
set pagesize 1000;
alter session set current_schema=APPS;


prompt INVALID OBJECTS AFTER DEPLOYMENT
CLEAR COLUMNS BREAKS COMPUTES
COLUMN owner   		FORMAT a15    HEADING 'Owner'
COLUMN object_name  FORMAT a35    HEADING 'Object Name'
COLUMN object_type  FORMAT a25    HEADING 'Object Type'

select owner,object_name,object_type  from apps.xxdba_invalid_objects_v;
spool off;

prompt Guardium Raporlama Bitir..
select 'GuardAppEvent:Released' from dual;
exit;"