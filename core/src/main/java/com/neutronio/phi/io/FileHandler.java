package com.neutronio.phi.io;

import com.badlogic.gdx.Gdx;
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

    /**
     * Enum that defines status of file operation in FileHandler implementations.
     */
    enum FileOperationStatus {
        /** File was saved successfully */
        SAVED(Message.MessageType.SUCCESS),
        /** File was loaded successfully */
        LOADED(Message.MessageType.SUCCESS),
        /** File was deleted successfully */
        DELETED(Message.MessageType.SUCCESS),
        /** File operation could not be executed and was aborted (e.g. because write protection is on) */
        ABORTED(Message.MessageType.WARNING),
        /** File operation critical error */
        FAILED(Message.MessageType.EXCEPTION);

        public final Message.MessageType messageType;

        FileOperationStatus(Message.MessageType messageType) {
            this.messageType = messageType;
        }
    }

    /**
     * Describes a file location and determines how file paths are interpreted.
     * Maps to FileHandle methods in {@link com.badlogic.gdx.Gdx#files}
     */
    enum FileLocation { // TODO replace with com.badlogic.gdx.Files.FileType
        /** Absolute path relative to the current drive */
        ABSOLUTE,
        /** Internal path relative to the projects, including internal resources */
        INTERNAL,
        /** Local path of the current drive, not including internal sources */
        LOCAL,
        /** Classpath i.e. "inside" the project, including assets and resources */
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
    FileOperationStatus saveFileContents(String data, String path, FileLocation location) throws IOException;

    /**
     * Saves text content to a text-based file
     * @param data String data
     * @param path the folder and filename to save to
     * @param location file location, determines how path is interpreted
     * @param overwrite if the file should be overwritten if it already exists
     * @return a status. SAVED if successful, ABORTED if file already exists and overwrite protection is on.
     * @throws IOException if the on error occurred closing the stream after an error usage
     */
    FileOperationStatus saveFileContents(String data, String path, FileLocation location, boolean overwrite) throws IOException;

    /**
     * Saves bytes to a binary file
     * @param savable a serializable class
     * @param path the folder and filename to save to
     * @param location file location, determines how path is interpreted
     * @return a status. SAVED if successful, ABORTED if file already exists.
     */
    FileOperationStatus saveFileBinary(Serializable savable, String path, FileLocation location) throws IOException;
}
