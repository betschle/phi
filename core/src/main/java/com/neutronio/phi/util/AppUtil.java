package com.neutronio.phi.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.neutronio.phi.PhiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.zip.Deflater;

/**
 * General App utilities.
 */
public class AppUtil {

    protected static Logger logger = Logger.getLogger(AppUtil.class.getCanonicalName());
    public static SimpleDateFormat screenshotDateFormat = new SimpleDateFormat("hh-mm-ss_dd-MM-yyyy");

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
            throw new PhiException(PhiException.ErrorCode.E1010,
                "Could not initialize logging with default config file: " + file.toPath());
        } finally {
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
            throw new PhiException(PhiException.ErrorCode.E1000, "version", e);
        } catch (IOException e) {
            throw new PhiException(PhiException.ErrorCode.E1010, e);
        }
    }

    /**
     *
     * @param userDirectory
     */
    public static void takeScreenshot(FileHandle userDirectory) {
        Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        ByteBuffer pixels = pixmap.getPixels();

        // This loop makes sure the whole screenshot is opaque and looks exactly like what the user is seeing
        int size = Gdx.graphics.getBackBufferWidth() * Gdx.graphics.getBackBufferHeight() * 4;
        for (int i = 3; i < size; i += 4) {
            pixels.put(i, (byte) 255);
        }

        FileHandle directory = userDirectory.child("/screenshots/");

        if( !directory.file().exists() ) {
            directory.file().mkdir();
        }

        FileHandle fileHandle = directory.child("screenshot_" + screenshotDateFormat.format( new Date() ) + ".jpg");
        PixmapIO.writePNG(fileHandle, pixmap, Deflater.DEFAULT_COMPRESSION, true);
        pixmap.dispose();
    }

    /**
     *
     */
    public static void takeScreenshot() {
        Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        ByteBuffer pixels = pixmap.getPixels();

        // This loop makes sure the whole screenshot is opaque and looks exactly like what the user is seeing
        int size = Gdx.graphics.getBackBufferWidth() * Gdx.graphics.getBackBufferHeight() * 4;
        for (int i = 3; i < size; i += 4) {
            pixels.put(i, (byte) 255);
        }

        String directory = "screenshots/";

        FileHandle directoryHandle = Gdx.files.local(directory);
        if( !directoryHandle.file().exists() ) {
            directoryHandle.file().mkdir();
        }

        String filename = directory +"screenshot_" + screenshotDateFormat.format(new Date());
        FileHandle fileHandle = Gdx.files.local(filename + ".jpg");

        PixmapIO.writePNG(fileHandle, pixmap, Deflater.DEFAULT_COMPRESSION, true);
        pixmap.dispose();
    }
}
