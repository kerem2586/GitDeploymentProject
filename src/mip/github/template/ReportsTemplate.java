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

public class ReportsTemplate implements Template {

    final String fileSeperator = System.getProperty("file.separator");
    static String shfileName = "deploy_reports.sh";

    @Override
    public void create(File mainFolder) {
        STGroup group = new STGroupDir("templates");
        ST header = group.getInstanceOf("reportheader");

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

        //url = this.getClass().getClassLoader().getResource("nls.properties");
        try {
            //properties.load(url.openStream());
            properties.load(new FileInputStream(new File("nls.properties")));
        } catch (FileNotFoundException ex) {
            DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ex);
        } catch (IOException ioex) {
            DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ioex);
        }

        List rdfList = new ArrayList();
        getRdfFiles(mainFolder, rdfList);
        for (Iterator it = rdfList.iterator(); it.hasNext();) {
            File rdfDir = (File) it.next();
            try {
                int rdfDirBeginIndex = rdfDir.getCanonicalPath().indexOf("rdf") + 4;
                String rdfFileDir = rdfDir.getCanonicalPath().substring(rdfDirBeginIndex);
                String[] rdfAttributes = rdfFileDir.split(Pattern.quote(fileSeperator));
                String language = rdfAttributes[1];
                String rdfName = (rdfAttributes[2].split(Pattern.quote(".")))[0];
                String applicationName = rdfAttributes[0];
                String applicationTop = rdfAttributes[0] + "_TOP";
                header.addAggr("rdfObjects.{ rdfName, language,applicationName ,applicationTop }", rdfName, language,applicationName,applicationTop);
            } catch (IOException ex) {
                DeploymentFactory.ignoredFiles.add(rdfDir);
                DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                DeploymentFactory.ignoredFiles.add(rdfDir);
                DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, e);
            }
        }
        

        char quotationMark = '"';
        header.add("doublequote", quotationMark);
    }
    
    private static void getRdfFiles(File reportDir, List rdfList) {
        File[] files = reportDir.listFiles();

        for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
            File file = (File) it.next();
            if (file.isFile() && Util.endsWithIgnoreCase(file.getName(), ".rdf") ) {
                rdfList.add(file);
            }

            if (file.isDirectory()) {
                getRdfFiles(file, rdfList);
            }
        }

    }

}
