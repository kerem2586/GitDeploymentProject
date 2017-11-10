/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mip.github.filter;

import java.io.File;
import java.io.FileFilter;

public class DeploymentFilter implements FileFilter {

    private final String[] okFileDirNames =
        new String[] {"fmb", "java", "fndload", "reports","db"};


    @Override
    public boolean accept(File file) {
        for (String extension : okFileDirNames) {
            if (file.getName().equalsIgnoreCase(extension)) {
                return true;
            }
        }

        return false;
   }

}
