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
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;
import mip.github.util.ArrayIterator;
import mip.github.util.Util;
import mip.github.deploymentFactory.DeploymentFactory;


public class JavaTemplate implements Template {

    final static String fileSeperator = System.getProperty("file.separator");
    static String shfileName = "deploy_java.sh";
    Boolean fileExists = false;

    @Override
    public void create(File mainFolder) {
        STGroup group = new STGroupDir("templates");
        ST header = group.getInstanceOf("javaheader");

        List javaList = new ArrayList();
        getJavaFiles(mainFolder, javaList);
        for (Iterator it = javaList.iterator(); it.hasNext();) {
            fileExists = true;
            String javaDir = (String) it.next();
            int javaDirBeginIndex = javaDir.indexOf("java")+5;
            String fileDirectory = javaDir.substring(javaDirBeginIndex);
            header.addAggr("javaObjects.{ folder }", fileDirectory);

        }              
        
        header.add("htmlExists", oahtmlExists(mainFolder.getParent()));
        List jspList = new ArrayList();
        if (oahtmlExists(mainFolder.getParent())) {
            getJspFiles(mainFolder.getParent(), jspList, 1);
            for (Iterator it = jspList.iterator(); it.hasNext();) {
                fileExists = true;
                File jspFile = (File) it.next();
                try {
                    int jspDirBeginIndex = jspFile.getCanonicalPath().indexOf("OA_HTML") + "OA_HTML".length() + 1;
                    String fileDirectory = jspFile.getCanonicalPath().substring(jspDirBeginIndex);
                    header.addAggr("jspObjects.{ jspName }", fileDirectory);
                } catch (IOException ex) {
                    DeploymentFactory.ignoredFiles.add(jspFile);
                    DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ex);
                } catch (Exception e) {
                    DeploymentFactory.ignoredFiles.add(jspFile);
                    DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, e);
                }
            }
        }
        header.add("mediaExists", oamediaExists(mainFolder.getParent()));

        char quotationMark = '"';
        header.add("doublequote", quotationMark);

        if (fileExists || oamediaExists(mainFolder.getParent()))
            Util.createDeploymentFile(mainFolder, header.render(), shfileName);

    }

    /*private static void getXmlFiles(File javaDir, List xmlList) {
        File[] files = javaDir.listFiles(new CustomCodeFilter());

        for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
            File file = (File) it.next();
            if (file.isFile() ) {
                //DeploymentFactory.deploymentLogger.log(Level.INFO, file.getParent()+"--"+file.getName());
                //if ((Util.endsWithIgnoreCase(file.getName(), "RN.xml") || Util.endsWithIgnoreCase(file.getName(), "PG.xml"))) {
                if (Util.endsWithIgnoreCase(file.getParent(), "webui") && Util.endsWithIgnoreCase(file.getName(), ".xml")) {
                    xmlList.add(file);
                }
            }
            if (file.isDirectory()) {
                getXmlFiles(file, xmlList);
            }
        }
    }*/

    private static void getJavaFiles(File javaDir, List javaList) {
        File[] files = javaDir.listFiles();

        for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
            File file = (File) it.next();
            if (file.isFile() && Util.endsWithIgnoreCase(file.getName(), ".java") ) {
                int exists = 0;
                for (Iterator itt = javaList.iterator(); itt.hasNext();) {
                    String path = (String) itt.next();
                    if (path.equals(file.getParent())) {
                        exists = 1;
                        break;
                    }
                }
                
                if (exists == 0)
                    javaList.add(file.getParent());
            }

            if (file.isDirectory()) {
                getJavaFiles(file, javaList);
            }
        }
    }        

    private static boolean oahtmlExists (String mainDir) {
        File mainDirectory = new File(mainDir+fileSeperator+"OA_HTML");
        File[] files = mainDirectory.listFiles();

        for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
            Object object = it.next();
            return true;
        }

        return false;
    }

    private static boolean oamediaExists (String mainDir) {
        File mainDirectory = new File(mainDir+fileSeperator+"OA_MEDIA");
        File[] files = mainDirectory.listFiles();

        for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
            Object object = it.next();
            return true;
        }

        return false;
    }

    private static void getJspFiles(String mainDir, List jspList, int firstCall) {
        File mainDirectory  = null;
        if (firstCall == 1)
            mainDirectory = new File(mainDir+fileSeperator+"OA_HTML");
        else
            mainDirectory = new File(mainDir);

        File[] files = mainDirectory.listFiles();

        for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
            File file = (File) it.next();
            if (file.isFile()) {
                if (Util.endsWithIgnoreCase(file.getName(), ".jsp")) {
                    jspList.add(file);
                }
            }

            if (file.isDirectory()) {
                try {
                    getJspFiles(file.getCanonicalPath(), jspList, 0);
                } catch (IOException ex) {
                    DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
