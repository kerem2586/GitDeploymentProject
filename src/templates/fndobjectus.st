fndobjectus(ldtObjectsUs) ::= "

<ldtObjectsUs:{it|
echo 'Uploading US <it.fndType>...'

FNDLOAD apps/$AppsPWD 0 Y UPLOAD <it.applTop>/patch/115/import/<it.lctFile> erp/ldt/<it.ldtFile>
<\n>}>"