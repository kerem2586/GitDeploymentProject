/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mip.github.template;

import java.io.File;

public class TemplateFactory {

    /*final static File currentDir = new File(".");*/

    public Template createDeployTemplate (File folder) {
        
        if (folder.getName().equalsIgnoreCase("java")) {
            return new JavaTemplate();
        } else if (folder.getName().equalsIgnoreCase("reports")) {
            return new ReportsTemplate();
        } else if (folder.getName().equalsIgnoreCase("fmb")) {
            return new FormsTemplate();
        } else if (folder.getName().equalsIgnoreCase("fndload")) {
            return new FndloadTemplate();
        } else if (folder.getName().equalsIgnoreCase("wft")) {
            return new WorkflowTemplate();
        } else if (folder.getName().equalsIgnoreCase("ws")) {
            return new IrepTemplate();
        } else if(folder.getName().equalsIgnoreCase("db")){
            return new DbTemplate();
        } else if(folder.getName().equalsIgnoreCase("pll")){
            return new PllTemplate();
        }
        else {
            return null;
        }
    }

}
