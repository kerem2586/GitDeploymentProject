dbheader(projectName,dbtblobjects,dbseqobjects,dbtypeobjects,dbpksobjects,dbprcobjects,dbfncobjects,dbvwobjects,dbtrgobjects,dbpkbobjects,dbtriggerobject,dbbdmlobjects,dbadmlobjects) ::= "
set define off
set serveroutput on
prompt Guardium Raporlama Baslat..
select 'GuardAppEvent:Start','GuardAppEventType:PROJECT','GuardAppEventStrValue:<projectName>' from dual;

prompt Deploy Oncesi Invalid Cek..

alter session set current_schema=APPS;

begin
apps.xxdba_start_deploy;
end;
/

<dbbdmlobjects(dbbdmlobjects)>
<dbtblobjects(dbtblobjects)>
<dbseqobjects(dbseqobjects)>
<dbtypeobjects(dbtypeobjects)>
<dbpksobjects(dbpksobjects)>
<dbprcobjects(dbprcobjects)>
<dbfncobjects(dbfncobjects)>
<dbvwobjects(dbvwobjects)>
<dbtrgobjects(dbtrgobjects)>
<dbpkbobjects(dbpkbobjects)>
<dbadmlobjects(dbadmlobjects)>
<dbfooter()>"