/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mip.github.deployment;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Collections.singleton;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import mip.github.filter.DeploymentFilter;
import mip.github.template.Template;
import mip.github.template.TemplateFactory;
import mip.github.util.ArrayIterator;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;


/**
 *
 * @author kdogan
 */
public class GitHubDeployment {
    public  static  ArrayList objectList = new ArrayList();
    public  static  String  gitPath =   "C:\\tmp";    
    private static  File    deploymentDirectory =   null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)   
    {
        // TODO code application logic here                
        String jiraNo = null ;
        try {
            jiraNo = args[0];// "BS-9999";
            System.out.println("Jira No: "+jiraNo);
        }
        catch(ArrayIndexOutOfBoundsException exception)
        {
            System.out.println("error while getting Parameter!"+exception.toString());
            System.exit(1);
        }                                                                               
        Git git = null    ;
        try {
            git = getGitAgent(gitPath,"master");
        } catch (IOException ex) {            
            System.out.println("error while getting Repository!"+ex.toString());
            System.exit(1);
        } catch (GitAPIException ex) {
            System.out.println("error while getting Repository!"+ex.toString());
            System.exit(1);
        } catch (InterruptedException ex) {
            System.out.println("error while getting Repository!"+ex.toString());
            System.exit(1);
        }        
        if  (   git !=  null    )
        { 
            Repository  repo    =   git.getRepository();
            RevWalk rw = new RevWalk(repo);                           
                                                                                         
            RevCommit   commit = null  ;
            try {
                commit = getLastCommitOnJira(git,jiraNo);
            } 
            catch (IOException ex) {
                System.out.println("error while getting commits!"+ex.toString());
                System.exit(1);
            } 
            catch (GitAPIException ex) {
                System.out.println("error while getting commits!"+ex.toString());
                System.exit(1);
            }  
            if  (commit   ==  null)
            {
                System.out.println("No suitable commit  found!");
                System.exit(1);
            }
            RevTree tree = commit.getTree();                    
            System.out.println("Commit Date: "+commit.getAuthorIdent().getWhen());
            System.out.println("Commit Name: "+commit.getName());
            System.out.println("Commit Id: "+commit.getId());
            System.out.println("Commit Message: "+commit.getFullMessage());
                            
            if  (   tree !=  null)
            {
                TreeWalk    walk = null;                                
                try {
                        deploymentDirectory =   getDeploymentFolder(jiraNo)   ; //  backUp zip  and create  new Deploy Directory    for jiraNo
                        populateObjectList(repo,commit,tree,rw,jiraNo);         //  populate    ObjectList  and create  objectList.txt                                                                        
                        printObjectName(jiraNo);                                //  write   objects in  deployment  folder  in  .git    hierarchy                        
                                       
                        TemplateFactory factory = new TemplateFactory();
                        
                        for (ArrayIterator it = new ArrayIterator(deploymentDirectory.listFiles(new DeploymentFilter())); it.hasNext();) 
                        {
                            File childFolder = (File) it.next();
                            Template template = factory.createDeployTemplate(childFolder);
                            template.create(childFolder);
                        }
                        
                        try {                                                   //  .zip    the     deployment  folder                
                            FileOutputStream fos = null;
                            fos = new FileOutputStream(new File(jiraNo+".zip"));
                            ZipOutputStream zos = new ZipOutputStream(fos);         
                            File deploymentDir = deploymentDirectory;   
                            compressDir(zos, deploymentDir.getParentFile());
                            zos.close();        
                            deleteDir(deploymentDir.getParentFile());
                        }catch(Exception ex){
                            ;
                        }
                } catch (CorruptObjectException ex) 
                {
                        rw.dispose();
                        System.out.println("error while getting Objects!"+ex.toString());
                        System.exit(1);
                } 
                catch (IOException ex) 
                {
                        rw.dispose();    
                        System.out.println("error while getting Objects!"+ex.toString());
                        System.exit(1);
                }                                                           
            
                /*TemplateFactory factory = new TemplateFactory(); 
                File[]  files   =   DeploymentDirectory.listFiles(new DeploymentFilter());
                for (ArrayIterator it = new ArrayIterator(DeploymentDirectory.listFiles(new DeploymentFilter())); it.hasNext();) 
                {
                    File childFolder = (File) it.next();
                    Template template = factory.createDeployTemplate(childFolder);
                    template.create(childFolder);
                }*/            
            }  
            
            else
            {                
                System.out.println("Commit Tree Not Found!");
                System.exit(1);
            }
            rw.dispose();
            System.exit(0);
        }                        
    }                                                                                                                                                        
    
    private static  Git getGitAgent(String  gitFolderPath,String    branchName)  throws  IOException, GitAPIException, InterruptedException
    {
        Git     git ;
        File    GitFolder   =   new File(gitFolderPath);
            
        if  (GitFolder.exists()) 
        {                   
            git = Git.open(GitFolder);  
            git.checkout().setName(branchName).call();            
            git.pull().setCredentialsProvider(new UsernamePasswordCredentialsProvider("bitbucket-admin", "MerBit4950w")).call();                                    
        }
        else
        {
            git =   Git.cloneRepository()                
                        .setURI("https://s117.mersinport.com.tr:8443/scm/ebs/oracleebsasgit.git")
                        .setCredentialsProvider( new UsernamePasswordCredentialsProvider("bitbucket-admin", "MerBit4950w"))
                        .setDirectory(GitFolder)
                        .setBranchesToClone( singleton( branchName ))
                        .setBranch(branchName)
                        .call();
        }            
        return   git;
    } 
    
    private static  ArrayList<RevCommit> getFilteredCommits(Git gitAgent,String  jiraNo) throws IOException, GitAPIException
    {
        ArrayList<RevCommit>    filteredCommitList =   new ArrayList<RevCommit>();                
        Iterable<RevCommit> commits = gitAgent.log().all().call();
        
        for (RevCommit commit : commits)
        {                                        
            if  (commit.getFullMessage().toUpperCase().startsWith(jiraNo.toUpperCase()))
            { 
                filteredCommitList.add(commit);
            }                     
        }        
        return  filteredCommitList;
    }  
    
    private static  RevCommit   getLastCommitOnJira(Git gitAgent,String jiraNo) throws  IOException,GitAPIException
    {
        ArrayList<RevCommit>    filteredCommitList =   new ArrayList<RevCommit>();                
        Iterable<RevCommit> commits = gitAgent.log().all().call();
        
        RevCommit   lastCommit  =   null;
        
        for (RevCommit commit : commits)
        {
            if(commit.getFullMessage().toUpperCase().startsWith(jiraNo.toUpperCase())){
                filteredCommitList.add(commit);
            }
        }        
        if  (filteredCommitList.size()    >   0)
        {
            lastCommit  =  filteredCommitList.get(0);
            for (   int i=0;i<filteredCommitList.size();i++   )    
            {
                if  (   filteredCommitList.get(i).getAuthorIdent().getWhen().compareTo(lastCommit.getAuthorIdent().getWhen())   >   0   )
                {
                    lastCommit  =   filteredCommitList.get(i);   
                }
            }
        }        
        return  lastCommit;
    }
    
    private static  void    populateObjectList  (   Repository  repo,
                                                    RevCommit   commit,
                                                    RevTree     commitTree,
                                                    RevWalk     revWalk,
                                                    String      jiraNo
    )   throws  CorruptObjectException, 
                IncorrectObjectTypeException, 
                IOException
    {
        TreeWalk    walk    =   new TreeWalk(repo);        
        walk.setRecursive(true);                                       
        walk.addTree(commitTree);
        walk.setFilter(TreeFilter.ANY_DIFF);
        
        for (RevCommit parent : commit.getParents()) 
        {
            revWalk.parseCommit(parent);            
            walk.addTree(parent.getTree());
        }                            
        while(  walk.next() )
        {                                 
            HashMap mMap = new HashMap();
            mMap.put("ObjectName",walk.getNameString());
            mMap.put("ObjectPath",walk.getPathString());            
            objectList.add(mMap);
        }
                
        if  (   deploymentDirectory.exists()    )
        {
            File file = new File(jiraNo+File.separator+"objectList.txt");        
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            String separator = System.getProperty("line.separator");                                            
            for (int a =0; a<objectList.size();a++)
            {
                HashMap<String, String> tmpData = (HashMap<String, String>) objectList.get(a);
                Set<String> key = tmpData.keySet();
                for (String hmKey : key) 
                {
                    String hmData = (String)tmpData.get(hmKey);                    
                    if  (hmKey.equals("ObjectPath"))
                    {
                        output.write(hmData);
                        output.write(separator);
                    }                                            
                }
            }        
        output.close();
        }        
    }  
    
    private static  File    getDeploymentFolder(String    jiraNo)
    {
        File checkFolder = new File(jiraNo+".zip");
        if  (   checkFolder.exists()  )    
        { 
            System.out.println("Directory already exists ..."); 
            System.out.println("BackUp  Directory"); 
            SimpleDateFormat sdfDate = new SimpleDateFormat("_yyyyMMddHHmmss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            File    backUp  =   new File(jiraNo+strDate+".zip");
            checkFolder.renameTo(backUp);                                 
        }
        File deploymentDir = new File(jiraNo+File.separator+"deployment");                   
        boolean success; 
        success = deploymentDir.mkdirs();
        if  (success)
        {
            return  deploymentDir;
        }
        else
        {
            return  null;
        }
    }    
    
    private static  void    printObjectName(String jiraNo) throws IOException 
    {                                                                           
        for (int a =0; a<objectList.size();a++)
        {
            HashMap<String, String> tmpData = (HashMap<String, String>) objectList.get(a);
            Set<String> key = tmpData.keySet();
            Iterator it = key.iterator();   
            String  FilePath    =   null;
            String  FileName    =   null;
            while (it.hasNext()) 
            {
                String hmKey  = (String)it.next();
                String hmData = (String)tmpData.get(hmKey);                  
                if  (hmKey.equals("ObjectPath"))
                {
                    FilePath    =   hmData;                                        
                }
                else if   (hmKey.equals("ObjectName"))   
                {
                    FileName    =   hmData;
                }
                it.remove(); // avoids a ConcurrentModificationException
            } 
            File    inputFile   =   new File(gitPath+File.separator+FilePath);
            if  (inputFile.exists())
            {
                try {
                    copyFile(inputFile,FileName,deploymentDirectory+File.separator+FilePath.replace("/",File.separator )) ;
                }catch(IOException  ex)
                {
                    System.out.println("Error Creating Object dirs copyFile");
                    System.exit(1);
                }
            }
        }       
    }        
    private static  void    copyFile(File  sourceFile, String   FileName,String    targetLocation) throws IOException 
    {              
        if  (sourceFile.exists())
        {
            InputStream in = null;
            try {
                in = new FileInputStream(sourceFile);
            } catch (FileNotFoundException ex) 
            {                
                System.out.println("FileNotFound: "+ex.toString());
                System.exit(1);
            }
            
            if  (   targetLocation !=  null )
            {                                                  
                int step            =   targetLocation.lastIndexOf(File.separator);
                String filePath     =   targetLocation.substring(0, step);                                
                                                                
                boolean success = new File(filePath).mkdirs();  
                
                if  (success)
                {
                    System.out.println("Directories are created!");
                    OutputStream out = null;
                    try {
                        File    outputFile  =   new File(filePath+File.separator+FileName);
                        out = new FileOutputStream(outputFile);
                        // Copy the bits from instream to outstream
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) 
                        {
                            out.write(buf, 0, len);
                        }
                    } 
                    catch (FileNotFoundException ex) 
                    {                        
                        System.out.println(ex.getLocalizedMessage());
                        System.out.println(ex.getMessage());
                        System.out.println(ex.toString());
                        System.exit(1);
                    }
                    finally {                    
                        in.close();
                        out.close();
                    }
                }
                else    
                {
                    System.out.println("Directory already created!");
                    OutputStream out = null;
                    try {
                        File    outputFile  =   new File(filePath+File.separator+FileName);
                        out = new FileOutputStream(outputFile);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) 
                        {
                            out.write(buf, 0, len);
                        }
                    } catch (FileNotFoundException ex) {                        
                        System.out.println(ex.getLocalizedMessage());
                        System.out.println(ex.getMessage());
                        System.out.println(ex.toString());
                        System.exit(1);
                    }
                    finally {                    
                        in.close();
                        out.close();
                    };
                }
            }
            in.close();
        }
    }
    
    public static void compressDir (ZipOutputStream zos, File dir)  
    {
        try {            
            File[] files = dir.listFiles();
            for (ArrayIterator it = new ArrayIterator(files); it.hasNext();)    
            {
                File file = (File) it.next();
                if  (file.isFile()) 
                {
                    FileInputStream fis = new FileInputStream(file.getCanonicalPath());                                                    
                    String path = file.getPath();                   
                    zos.putNextEntry(new ZipEntry(path));
                    int size = 0;
                    byte[] buffer = new byte[1024];                    
                    while ((size = fis.read(buffer, 0, buffer.length)) > 0) {
                        zos.write(buffer, 0, size);
                    }
                    zos.closeEntry();
                    fis.close();
                } else {                    
                    compressDir(zos, file);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) 
    {
        if (dir.isDirectory()) 
        {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) 
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }    
    
}                
