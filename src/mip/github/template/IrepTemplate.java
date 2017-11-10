/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mip.github.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.regex.Pattern;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;
import mip.github.deploymentFactory.DeploymentFactory;
import mip.github.util.ArrayIterator;
import mip.github.util.Util;

public class IrepTemplate implements Template {

    final String fileSeperator = System.getProperty("file.separator");
    static String shfileName = "deploy_ws.sh";
    Boolean exists = false;

    @Override
    public void create(File folder) {
        STGroup group = new STGroupDir("templates");
        ST header = group.getInstanceOf("irepwsheader");

        File[] files = null;

        for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
            File ff = (File)it.next();
            if (ff.isFile()) {
                try {
                    String f = readFileAsString(ff.getCanonicalPath());
                    String applicationShortName = ((f.split(Pattern.quote("* @rep:product ")))[1].split(Pattern.quote("\n")))[0];
                    String fileName = ff.getName().split(Pattern.quote("."))[0];

                    header.addAggr("wsObjects.{ fileName, applTop }", fileName, applicationShortName);

                    exists = true;
                } catch (IOException ex) {
                    DeploymentFactory.ignoredFiles.add(ff);
                    DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ex);
                } catch (Exception e) {
                    DeploymentFactory.ignoredFiles.add(ff);
                    DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, e);
                }
            }
        }
        if (exists)
            Util.createDeploymentFile(folder, header.render(), shfileName);
        
    }

    private static String readFileAsString(String filePath) throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;

        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();
        return fileData.toString();
    }

}
