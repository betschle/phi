package com.neutronio.phi.io;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.neutronio.phi.app.Message;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * An interface for low-level file handling. Reads and saves contents to a file. Allows for
 * string and binary file support. Formerly AstraXFiles.
 */
public interface FileHandler {

    // TODO translation should be handled differently here, use translation modifier by name convention, e.g. STATE_SAVED
    // TODO Rename to FileHandleStatus ? FileOperationStatus ?
    enum State {
        SAVED(Message.MessageType.SUCCESS, ""), //AstraXAppTranslations.FILE_SAVED_SUCCESS
        DELETED(Message.MessageType.SUCCESS, ""), // AstraXAppTranslations.FILE_DELETED_SUCCESS
        ABORTED(Message.MessageType.WARNING, ""), // AstraXAppTranslations.FILE_OPERATION_ABORTED
        FAILED(Message.MessageType.EXCEPTION, ""); // AstraXAppTranslations.FILE_OPERATION_FAILED

        public final Message.MessageType messageType;
        public final String message;

        State(Message.MessageType messageType, String messageTranslation) {
            this.messageType = messageType;
            this.message = messageTranslation;
        }
    }

    enum FileLocation {
        ABSOLUTE,
        INTERNAL,
        /** Contained in the current datapack. Not yet implemneted*/
        DATAPACK,
        LOCAL,
        CLASSPATH,
        EXTERNAL;
    }

    /**
     * Attempts to delete a file
     * @param file
     */
    boolean deleteFile(GDXFileHandler.PhiFile file);

    /**
     * Reads folders and files from a directory.
     * @param path
     * @param location the file location
     * @param glob
     * @return a list of files or folders that are contained in path
     * @throws IOException
     * @throws GdxRuntimeException
     */
    List<GDXFileHandler.PhiFile> readFilesInDirectory(String path, FileLocation location, String glob) throws IOException, GdxRuntimeException;
    /**
     * Reads the contents of a text-based file
     * @param path the folder and filename to save to
     * @param location file location, determines how path is interpreted
     * @return
     */
    StringBuilder readFileContents(String path, FileLocation location) throws IOException, GdxRuntimeException;

    /**
     * Reads the contents of a binary file
     * @param path the folder and filename to save to
     * @param location file location, determines how path is interpreted
     * @return
     */
    <T extends Serializable> T readFileBinary(String path, FileLocation location) throws IOException;

    /**
     * Saves text content to a text-based file
     * @param data String data
     * @param path the folder and filename to save to
     * @param location file location, determines how path is interpreted
     * @return a status message
     */
    State saveFileContents(String data, String path, FileLocation location) throws IOException;

    /**
     * Saves text content to a text-based file
     * @param data String data
     * @param path the folder and filename to save to
     * @param location file location, determines how path is interpreted
     * @param overwrite if the file should be overwritten if it already exists
     * @return a status. SAVED if successful, ABORTED if file already exists and overwrite protection is on.
     * @throws IOException if the on error occurred closing the stream after an error usage
     */
    State saveFileContents(String data, String path, FileLocation location, boolean overwrite) throws IOException;

    /**
     * Saves bytes to a binary file
     * @param savable a serializable class
     * @param path the folder and filename to save to
     * @param location file location, determines how path is interpreted
     * @return a status. SAVED if successful, ABORTED if file already exists.
     */
    State saveFileBinary(Serializable savable, String path, FileLocation location) throws IOException;
}
