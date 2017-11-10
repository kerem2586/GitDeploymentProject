/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mip.github.deploymentFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class DeploymentFactory {


    public static List ignoredFiles = new ArrayList<File>();
    public final static Logger deploymentLogger = Logger.getLogger("deploymentLogger");
    
    static void createLogger (String name) {
        LogManager manager= LogManager.getLogManager();
        Logger logger = manager.getLogger("global");

        try {
            FileHandler fh = new FileHandler(name);
            deploymentLogger.addHandler(fh);
            logger.addHandler(fh);
            deploymentLogger.setLevel(Level.ALL);
            deploymentLogger.setUseParentHandlers(false);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException ex) {
            Logger.getLogger(DeploymentFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(DeploymentFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void closeLogger() {
        Handler[] handlers = deploymentLogger.getHandlers();
        for (int i=0;i<handlers.length;i++) {
            FileHandler handler = (FileHandler) handlers[i];
            handler.close();
        }
    }
    
}
