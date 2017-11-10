/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mip.github.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import mip.github.deploymentFactory.DeploymentFactory;

public class Util {

    static String[] fndObjectTypes = {"alert", "attachment", "concurrent", "flexvalue", "function", "lookup", "menu", "message", "profile", "xdo"};
    final static String fileSeperator = System.getProperty("file.separator");
    
    public static boolean endsWithIgnoreCase (String str, String suf) {
        if (str.toLowerCase().endsWith(suf.toLowerCase())) {
            return true;
        }
        
        return false;
    }

    public static void loadFndPropertiesFile () {
        Properties properties = new Properties();

        // Write properties file.
        try {
            properties.put("alert", "$ALR_TOP#alr.lct");
            properties.put("attachment", "$FND_TOP#afattach.lct");
            properties.put("concurrent", "$FND_TOP#afcpprog.lct");
            properties.put("flexvalue", "$FND_TOP#afffload.lct");
            properties.put("function", "$FND_TOP#afsload.lct");
            properties.put("lookup", "$FND_TOP#aflvmlu.lct");
            properties.put("menu", "$FND_TOP#afsload.lct");
            properties.put("message", "$FND_TOP#afmdmsg.lct");
            properties.put("profile","$FND_TOP#afscprof.lct");
            properties.put("xdo", "$XDO_TOP#xdotmpl.lct");
 
            properties.store(new FileOutputStream("fndload.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadNlsLanguages () {
        Properties properties = new Properties();

        // Write properties file.
        try {
            properties.put("TR", "Turkish_Turkey.WE8ISO8859P9");
            properties.put("US", "American_America.WE8ISO8859P9");
            properties.put("RU", "Russian_Russia.WE8ISO8859P9");

            properties.store(new FileOutputStream("nlslanguage.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadNlsProperties () {
        Properties properties = new Properties();

        // Write properties file.
        try {
            properties.put("ISO-TR", "tr");
            properties.put("ISO-US", "en");

            properties.store(new FileOutputStream("nls.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void createDeploymentFile (File mainFolder, String script, String shfileName) {
        boolean success = false;
        try {
            success = (new File(mainFolder.getParentFile().getParent() + fileSeperator + shfileName)).createNewFile();
        } catch (IOException ex) {
            DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ex);
        }

        try {
            // Create file
            FileWriter fstream = new FileWriter(mainFolder.getParentFile().getParent() + fileSeperator + shfileName);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(script);
            appendToCallerScript(mainFolder.getParentFile().getParent() + fileSeperator, shfileName);
            //Close the output stream
            out.close();
        } catch (Exception e){
            success = false;
            DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, e);
        }

        if (success) {
            DeploymentFactory.deploymentLogger.log(Level.INFO, "{0} created.", shfileName);
        }
    }
    /*bu bilgilerin bir template dosyasından alınması gerekli*/
    static void writeInitialShValues(String filepath,boolean exists) throws IOException{
       FileWriter callerScript = new FileWriter(filepath, exists);
       BufferedWriter out = new BufferedWriter(callerScript);
       Date currentTime = GregorianCalendar.getInstance().getTime();
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z" );
       out.write("#"+sdf.format(currentTime));
       out.newLine();
       out.write("#birinci parametre apps sifresi");
       out.newLine();
       out.write("#ikinci parametre database deployment useri");
       out.newLine();
       out.write("#ucuncu parametre database deployment sifresi");
       out.newLine();
       out.write("#paketteki deployment tiplerine gore alt deployment scriptleri");
       out.newLine();
       out.write("#uncomment edilmistir");
       out.newLine();
       out.write("#sh deploy_db_objects.sh $2 $3");
       out.newLine();
       out.write("#sh deploy_pll.sh $1");
       out.newLine();
       out.write("#sh deploy_forms.sh $1");
       out.newLine();
       out.write("#sh deploy_reports.sh $1");
       out.newLine();
       out.write("#sh deploy_wf.sh $1");
       out.newLine();
       out.write("#sh deploy_ldt.sh $1");
       out.newLine();
       out.write("#sh deploy_java.sh $1");
       out.newLine();
       out.close(); 
    }
    static void appendToCallerScript (String dir, String message) 
    {
        if (Util.endsWithIgnoreCase(message, ".sql"))
        {
            return;
        }
        try {
            boolean exists = true;
            if (!(new File(dir+"deploy.sh").exists())) {
                exists = false;
            }
            if (exists == false) {
                writeInitialShValues(dir+"deploy.sh", exists);
            }
            BufferedReader file = new BufferedReader(new FileReader(dir+"deploy.sh"));
            String line;String input = "";
            while ((line = file.readLine()) != null){
                if(line.indexOf(message) != -1)
                {
                    input +=  line.substring(1)+System.lineSeparator();
                }else{
                    input +=  line + System.lineSeparator();    
                }
            }
            file.close();
            FileOutputStream File = new FileOutputStream(dir+"deploy.sh");
            File.write(input.getBytes());
            File.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ioex);
        }
    }

    public static void removeCallerScript (String dir) {
        File directory = new File(dir);

        try {
            if ((new File(directory.getCanonicalPath()+File.separator+"deploy.sh").exists())) {
                boolean success = new File(directory.getCanonicalPath()+File.separator+ "deploy.sh").delete();
                if (success) {
                    String err = "Deleted";
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /*public static void addToIgnoredFiles (File mainFolder, List<File> ignoredFiles) {
        File[] files = mainFolder.listFiles(new IgnoreFilter());
        for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
            File file = (File) it.next();
            ignoredFiles.add(file);
        }

    }*/
    public static void logIgnoredFiles (File mainFolder, List<File> ignoredFiles) {
        try {
            FileWriter fstream = new FileWriter(mainFolder.getParent() + fileSeperator + "ignoredFiles.log");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("Ignored Files: ");
            out.newLine();
            for (Iterator it = ignoredFiles.iterator(); it.hasNext();) {
                File file = (File) it.next();
                out.write(file.getCanonicalPath());
                out.newLine();
            }
            out.close();
        } catch (IOException ex) {
            DeploymentFactory.deploymentLogger.log(Level.SEVERE, null, ex);
        }
        
    }

    // Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

    public static void addFileToZip (File zipFile, File existingFile) throws FileNotFoundException, IOException {
        if (existingFile.isFile()) {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            FileInputStream fis = new FileInputStream(existingFile.getCanonicalPath());
            
            zos.putNextEntry(new ZipEntry(existingFile.getPath()));
            int size = 0;
            byte[] buffer = new byte[1024];

            // read data to the end of the source file and write it to the zip
            // output stream.
            while ((size = fis.read(buffer, 0, buffer.length)) > 0) {
                zos.write(buffer, 0, size);
            }

            zos.closeEntry();
            fis.close();

            zos.close();
        }
    }

    public static void compressDir (ZipOutputStream zos, File dir) {

	try {
            
            File[] files = dir.listFiles();
            for (ArrayIterator it = new ArrayIterator(files); it.hasNext();) {
                File file = (File) it.next();
                if (file.isFile()) {
                    FileInputStream fis = new FileInputStream(file.getCanonicalPath());

                    String path = file.getPath().substring(file.getPath().indexOf("packages")+"packages".length()+1);
                    // put a new ZipEntry in the ZipOutputStream
                    zos.putNextEntry(new ZipEntry(path));
                    int size = 0;
                    byte[] buffer = new byte[1024];

                    // read data to the end of the source file and write it to the zip
                    // output stream.
                    while ((size = fis.read(buffer, 0, buffer.length)) > 0) {
                        zos.write(buffer, 0, size);
                    }

                    zos.closeEntry();
                    fis.close();
                } else {
                    compressDir(zos, file);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
   }
}
