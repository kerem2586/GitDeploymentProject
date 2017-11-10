/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mip.github.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.regex.Pattern;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;
import mip.github.deploymentFactory.DeploymentFactory;
import mip.github.util.ArrayIterator;
import mip.github.util.Util;

public class WorkflowTemplate implements Template {

    final String fileSeperator = System.getProperty("file.separator");
    static String shfileName = "deploy_wf.sh";

    @Override
    public void create(File mainFolder) {
        STGroup group = new STGroupDir("templates");
        ST header = group.getInstanceOf("wftheader");

        Properties properties = new Properties();
        //URL url = this.getClass().getClassLoader().getResource("nlslanguage.properties");
        try {
            //properties.load(url.openStream());
            properties.load(new FileInputStream(new File("nlslanguage.properties")));
        } catch (FileNotFoundException ex) {
            DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ex);
        } catch (IOException ioex) {
            DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ioex);
        }

        List wftList = new ArrayList();
        getWftFiles(mainFolder, wftList);
        for (Iterator it = wftList.iterator(); it.hasNext();) {
            File wftDir = (File) it.next();
            try {
                int wftDirBeginIndex = wftDir.getCanonicalPath().indexOf("wft") + 4;
                String wftFileDir = wftDir.getCanonicalPath().substring(wftDirBeginIndex);
                String[] wftAttributes = wftFileDir.split(Pattern.quote(fileSeperator));
                String language = wftAttributes[0];
                String wftName = (wftAttributes[1].split(Pattern.quote(".")))[0];
                String nlsLanguage = properties.getProperty(language);
                header.addAggr("wftObjects.{ wftName, language, nlsLanguage }", wftName, language, nlsLanguage);
            } catch (IOException ex) {
                DeploymentFactory.ignoredFiles.add(wftDir);
                DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                DeploymentFactory.ignoredFiles.add(wftDir);
                DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, e);
            }

        }
        if (wftList.size() >0)
            Util.createDeploymentFile(mainFolder, header.render(), shfileName);
        
    }

    private static void getWftFiles(File wfDir, List wfList) {
        File[] files = wfDir.listFiles();

        for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
            File file = (File) it.next();
            if (file.isFile()) {
                if (Util.endsWithIgnoreCase(file.getName(), ".wft")) {
                    wfList.add(file);
                } else {
                    DeploymentFactory.ignoredFiles.add(file);
                }
            }

            if (file.isDirectory()) {
                getWftFiles(file, wfList);
            }
        }

    }


}
