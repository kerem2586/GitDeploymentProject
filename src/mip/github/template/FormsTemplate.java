/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mip.github.template;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;
import mip.github.deploymentFactory.DeploymentFactory;
import mip.github.util.ArrayIterator;
import mip.github.util.Util;

public class FormsTemplate implements Template {

    final String fileSeperator = System.getProperty("file.separator");
    static String shfileName = "deploy_forms.sh";

    @Override
    public void create(File mainFolder) {
        STGroup group = new STGroupDir("templates");
        ST header = group.getInstanceOf("formheader");

        List formList = new ArrayList();
        getFormFiles(mainFolder, formList);
        for (Iterator it = formList.iterator(); it.hasNext();) {
            File formDir = (File) it.next();
            try {
                int formDirBeginIndex = formDir.getCanonicalPath().indexOf("fmb") + 4;
                String formFileDir = formDir.getCanonicalPath().substring(formDirBeginIndex);
                String[] formAttributes = formFileDir.split(Pattern.quote(fileSeperator));
                String applicationName = formAttributes[0];
                String applicationTop = formAttributes[0] + "_TOP";
                String language = formAttributes[1];
                String formName = (formAttributes[2].split(Pattern.quote(".")))[0];
                header.addAggr("formObjects.{ formName, language ,applicationName ,applicationTop}", formName, language,applicationName,applicationTop);
            } catch (IOException ex) {
                DeploymentFactory.ignoredFiles.add(formDir);
                DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                DeploymentFactory.ignoredFiles.add(formDir);
                DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, e);
            }

        }

        //System.out.println(header.render());
        if (formList.size() > 0)
            Util.createDeploymentFile(mainFolder, header.render(), shfileName);
        
    }

    private void getFormFiles(File formsDir, List formList) {
        File[] files = formsDir.listFiles();

        for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
            File file = (File) it.next();
            if (file.isFile()) {
                if (Util.endsWithIgnoreCase(file.getName(), ".fmb")) {
                    formList.add(file);
                } else {
                    DeploymentFactory.ignoredFiles.add(file);
                }
            }

            if (file.isDirectory()) {
                getFormFiles(file, formList);
            }
        }
    }
}
