db_main() ::= "{

XNLS=$NLS_LANG
NLS_LANG=American_America.WE8ISO8859P9
export NLS_LANG

sqlplus $1/$2 @deploy_db_objects.sql

NLS_LANG=$XNLS
export NLS_LANG

} 2>&1 | tee deploy_db_object.log
"