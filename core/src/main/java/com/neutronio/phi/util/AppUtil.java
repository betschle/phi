package com.neutronio.phi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * General App utilities.
 */
public class AppUtil {

    protected static Logger logger = Logger.getLogger(AppUtil.class.getCanonicalName());

    /**
     * Initializes up logging via JUL. Expects a logging.properties file
     * in root directory.
     */
    public void setupLogging() {
        FileInputStream fis = null;
        File file = Paths.get("logging.properties").toFile();
        try {
            LogManager logManager = LogManager.getLogManager();
            fis = new FileInputStream(file);
            logManager.readConfiguration( fis  );
            fis.close();
            logger.log(Level.INFO, "Logging initialized!");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not initialize logging with default config file: " + file.toPath(), e);
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Reads out the version from a version file that contains the version string.
     * @return null if an error occurred
     */
    public String readVersion() {
        this.logger.log(Level.INFO, "Reading Version...");
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("version"));
            return new String(encoded, Charset.forName("UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
