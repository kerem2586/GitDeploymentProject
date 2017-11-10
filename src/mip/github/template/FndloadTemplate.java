/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mip.github.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.regex.Pattern;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;
import mip.github.util.ArrayIterator;
import mip.github.util.Util;
import mip.github.deploymentFactory.DeploymentFactory;

public class FndloadTemplate implements Template {

    final String fileSeperator = System.getProperty("file.separator");
    static String shfileName = "deploy_ldt.sh";

    @Override
    public void create(File mainFolder) {
        STGroup group = new STGroupDir("templates");
        ST header = group.getInstanceOf("fndloadheader");

        Properties properties = new Properties();
        URL url = this.getClass().getClassLoader().getResource("fndload.properties");
        try {
            //properties.load(url.openStream()); java versiyonuna gore bu kod
            // kullanılmak durumuna kalınabilir.
            properties.load(new FileInputStream(new File("fndload.properties")));
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ioex) {
            System.out.println(ioex.toString());
        } 

        List ldtList = new ArrayList();
        getLdtFiles(mainFolder, ldtList);
        for (Iterator it = ldtList.iterator(); it.hasNext();) {
            File ldtDir = (File) it.next();
            try {
                int ldtDirBeginIndex = ldtDir.getCanonicalPath().indexOf("ldt")+4;
                String fileDirectory = ldtDir.getCanonicalPath().substring(ldtDirBeginIndex);
                String[] dirAttributes = fileDirectory.split(Pattern.quote(fileSeperator));
                String fndType = dirAttributes[0];
                String language = dirAttributes[1];
                String[] fndAttributes = properties.getProperty(fndType).split("#");
                if (language.equalsIgnoreCase("US")) {
                    header.addAggr("ldtObjectsUs.{ fndType, lctFile, ldtFile, applTop }", fndType, fndAttributes[1], fileDirectory, fndAttributes[0]);
                } else if (language.equalsIgnoreCase("TR")) {
                    header.addAggr("ldtObjectsTr.{ fndType, lctFile, ldtFile, applTop }", fndType, fndAttributes[1], fileDirectory, fndAttributes[0]);
                }
            } catch (IOException ex) {
                DeploymentFactory.ignoredFiles.add(ldtDir);
                DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                DeploymentFactory.ignoredFiles.add(ldtDir);
                DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, e);
            }
        }
        
        if (ldtList.size() >0)
            Util.createDeploymentFile(mainFolder, header.render(), shfileName);
        
    }

    private void getLdtFiles(File ldtDir, List ldtList) {
        File[] files = ldtDir.listFiles();

        for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
            File file = (File) it.next();
            if (file.isFile() ) {
                if (Util.endsWithIgnoreCase(file.getName(), ".ldt")) {
                    ldtList.add(file);
                } else {
                    DeploymentFactory.ignoredFiles.add(file);
                }
            }

            if (file.isDirectory()) {
                getLdtFiles(file, ldtList);
            }
        }
    }
}
