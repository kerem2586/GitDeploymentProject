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

public class PllTemplate implements Template {

    final String fileSeperator = System.getProperty("file.separator");
    final String textDot = ".";
    static String shfileName = "deploy_pll.sh";

    @Override
    public void create(File mainFolder) {
        STGroup group = new STGroupDir("templates");
        ST header = group.getInstanceOf("pllheader");

        List pllList = new ArrayList();
        getPllFiles(mainFolder, pllList);
        for (Iterator it = pllList.iterator(); it.hasNext();) {
            File pllDir = (File) it.next();
            try {
                int pllDirBeginIndex = pllDir.getCanonicalPath().indexOf("pll") + 4;
                String[] pllAttributes = pllDir.getCanonicalPath().substring(pllDirBeginIndex).split(Pattern.quote(textDot));
                String pllName = pllAttributes[0];
                header.addAggr("pllObjects.{pllName}", pllName);
            } catch (IOException ex) {
                DeploymentFactory.ignoredFiles.add(pllDir);
                DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                DeploymentFactory.ignoredFiles.add(pllDir);
                DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, e);
            }
        }

        //System.out.println(header.render());
        if (pllList.size() > 0)
            Util.createDeploymentFile(mainFolder, header.render(), shfileName);
        
    }

    private void getPllFiles(File pllDir, List pllList) {
        File[] files = pllDir.listFiles();

        for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
            File file = (File) it.next();
            if (file.isFile()) {
                if (Util.endsWithIgnoreCase(file.getName(), ".pll")) {
                    pllList.add(file);
                } else {
                    DeploymentFactory.ignoredFiles.add(file);
                }
            }

            if (file.isDirectory()) {
                getPllFiles(file, pllList);
            }
        }
    }
}
