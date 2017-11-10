pllobject(pllObjects) ::= "
<\n>
<pllObjects:{it|
echo 'Backup <it.pllName>.plx and <it.pllName>.pll'
cp $AU_TOP/resource/<it.pllName>.plx $AU_TOP/resource/<it.pllName>.plx_$CurrentDate 
cp $AU_TOP/resource/<it.pllName>.pll $AU_TOP/resource/<it.pllName>.pll_$CurrentDate 

echo 'Copy <it.pllName>.pll'

cp erp/pll/<it.pllName>.pll $AU_TOP/resource/<it.pllName>.pll

cd $AU_TOP/resource

echo 'Compile <it.pllName>.pll'
f60gen <it.pllName>.pll apps/$AppsPWD module_type=LIBRARY compile_all=YES

cd $lCURRENTDIR
<\n>}>"
