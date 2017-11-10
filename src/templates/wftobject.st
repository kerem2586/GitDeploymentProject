wftobject(wftObjects) ::= "
<\n>
<wftObjects:{it|
NLS_LANG=<it.nlsLanguage>
export NLS_LANG

echo 'Uploading <it.language> Workflows...'
WFLOAD apps/$AppsPWD 0 Y UPLOAD erp/wft/<it.language>/<it.wftName>.wft
<\n>}>"
