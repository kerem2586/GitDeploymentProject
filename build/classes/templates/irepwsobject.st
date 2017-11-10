irepwsobject(wsObjects) ::= "
<! Erdem !>
<\n>
<wsObjects:{it|
cd $lCURRENTDIR
cp EBS/ws/<it.fileName>.pls /usr/tmp/irep_$irepTempFolder
cd $<it.applTop>_TOP/patch/115/sql
$IAS_ORACLE_HOME/perl/bin/perl $FND_TOP/bin/irep_parser.pl -g -v -username=sysadmin <it.applTop>:patch/115/sql:<it.fileName>.pls:12.0=/usr/tmp/irep_$irepTempFolder/<it.fileName>.pls
$FND_TOP/bin/FNDLOAD apps/$AppsPWD 0 Y UPLOAD $FND_TOP/patch/115/import/wfirep.lct <it.fileName>_pls.ildt
cd $lCURRENTDIR
<\n>}>"
