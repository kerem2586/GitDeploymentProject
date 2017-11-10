fndobjecttr(ldtObjectsTr) ::= "
<\n>

<ldtObjectsTr:{it|
echo 'Uploading TR <it.fndType>...'

FNDLOAD apps/$AppsPWD 0 Y UPLOAD <it.applTop>/patch/115/import/<it.lctFile> erp/ldt/<it.ldtFile> UPLOAD_MODE=NLS
<\n>}>"