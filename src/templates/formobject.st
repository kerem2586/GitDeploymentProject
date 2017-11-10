formobject(formObjects) ::= "
<\n>
<formObjects:{it|
echo 'Backup <it.applicationName> <it.language> <it.formName>.fmb'
cp $<it.applicationTop>/forms/<it.language>/<it.formName>.fmb $<it.applicationTop>/forms/<it.language>/<it.formName>_$CurrentDate.fmb

echo 'Copy <it.applicationName> <it.language> <it.formName>.fmb'
cp erp/fmb/<it.applicationName>/<it.language>/<it.formName>.fmb $<it.applicationTop>/forms/<it.language>/<it.formName>.fmb

cd $<it.applicationTop>/forms/<it.language>

echo 'Compile <it.applicationName> <it.language> <it.formName>.fmb'
f60gen userid=apps/$AppsPWD module=<it.formName>.fmb  output_file=<it.formName>.fmx

cd $lCURRENTDIR
<\n>}>"
