/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mip.github.template;

import mip.github.util.Util;
import java.io.File;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;


public class DbTemplate implements Template {

    @Override
    public void create(File mainFolder) {
        String dbFileName = "deploy_db_objects.sql";
        String shFileName = "deploy_db_objects.sh";
        boolean existsFlag = false;
        STGroup group = new STGroupDir("./templates");
        ST header = group.getInstanceOf("dbheader");
        ST sqlPlusHeader = group.getInstanceOf("db_main");
                
        String objectName;
        String objectType;
        
        if (mainFolder.isDirectory()) {
            for(File child : mainFolder.listFiles()) {
                if (child.isDirectory()) {
                    for(File file : child.listFiles()) 
                    {
                        objectName = file.getName().substring(0, file.getName().indexOf("."));
                        objectType = file.getName().substring(file.getName().indexOf(".")+1);
                        try {
                            header.addAggr("db"+objectType+"objects.{ objectName, objectType }",
                                                  objectName,
                                                  objectType);
                            existsFlag = true;
                            
                        }catch(Exception    ex){
                            System.out.println(ex.toString());
                            System.out.println(ex.getLocalizedMessage());
                        }
                    }
                } else {
                    header.addAggr("objects.{ objectName, objectType, objectTypeDesc }", child.getName(), child.getName(), child.getName());
                }
            }
            if  (existsFlag)
            {
                Util.createDeploymentFile(mainFolder, header.render(), dbFileName);
                Util.createDeploymentFile(mainFolder,sqlPlusHeader.render(), shFileName);
            }
        }
    }
}
