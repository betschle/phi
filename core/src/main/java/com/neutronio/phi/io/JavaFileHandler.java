package com.neutronio.phi.io;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.neutronio.phi.app.Message;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A FileHandler in plain old java, without gdx framework dependencies.
 * WARNING: for now, fileLocation parameter is consistently ignored here, implement it if necessary!
 */
public class JavaFileHandler implements FileHandler {

    // TODO write automated tests!
    // TODO This filehandler does not wark in conjunction with astrax blueprint loader
    // TODO confirm this handler works in packaged state as well (internal and external resources need to be able to be referenced)
    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    @Override
    public boolean deleteFile(GDXFileHandler.PhiFile file) {
        return file.file.delete();
    }

    @Override
    public List<GDXFileHandler.PhiFile> readFilesInDirectory(String path, FileLocation location, String glob) throws IOException, GdxRuntimeException {
        logger.fine("Reading files in directory: " + path);
        File rootDirectory = new File(path); // location?
        DirectoryStream<Path> dirs = Files.newDirectoryStream(rootDirectory.toPath(), glob);
        List<GDXFileHandler.PhiFile> directories = new ArrayList<>();
        Iterator<Path> iterator = dirs.iterator();
        while (iterator.hasNext()) {
            Path directory = iterator.next();
            directories.add(new GDXFileHandler.PhiFile(directory.toFile(), path, location));
        }
        dirs.close();
        return directories;
    }

    @Override
    public StringBuilder readFileContents(String path, FileLocation location) throws IOException {
        logger.log(Level.FINE, "Reading file contents: {0} {1}", new Object[]{path, location.name()});
        InputStream stream = null;
        StringBuilder builder = new StringBuilder();
        switch (location) {
            case INTERNAL:
            case CLASSPATH: {
                stream = this.getClass().getClassLoader().getResourceAsStream(path); // confirmed to work
                break;
            }
            default: {
                stream = new FileInputStream(path); // confirmed to work
                break;
            }
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String currentLine = "";
        while ((currentLine = reader.readLine()) != null) {
            builder.append(currentLine);
        }
        reader.close();
        return builder;
    }

    @Override
    public FileOperationStatus saveFileContents(String data, String path, FileLocation location) throws IOException {
        return saveFileContents(data, path, location, false);
    }

    @Override
    public FileOperationStatus saveFileContents(String data, String path, FileLocation location, boolean overwrite) throws IOException {
        // Note: cannot save to file locations INTERNAL, CLASSPATH
        logger.log(Level.FINE, "Save file contents to: {0} {1}", new Object[]{path, location.name()});
        File file = new File(path);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(data);

            if (writer != null)
                writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return FileOperationStatus.FAILED;
        }
        return FileOperationStatus.SAVED;
    }

    // TODO valid file locations are: ABSOLUTE, LOCAL, EXTERNAL (, DATAPACK)
    public FileOperationStatus saveFileBinary(Serializable savable, String path, FileLocation location) {
        logger.log(Level.FINE, "Save file binary to: {0} {1}", new Object[]{path, location.name()});
        FileOutputStream fileOutputStream = null;
        FileOperationStatus fileOperationStatus = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(savable);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOperationStatus = FileOperationStatus.SAVED;
        } catch (IOException e) {
            e.printStackTrace();
            fileOperationStatus = FileOperationStatus.FAILED;
        }
        return fileOperationStatus;
    }


    // TODO valid file locations are: ABSOLUTE, LOCAL, EXTERNAL (, DATAPACK)
    @Override
    public <T extends Serializable> T readFileBinary(String path, FileLocation location) {
        logger.log(Level.FINE, "Read file binary from: {0} {1}", new Object[]{path, location.name()});
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            T loaded = (T) objectInputStream.readObject();
            objectInputStream.close();
            return loaded;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PropertyResourceBundle loadBundle(String path, FileLocation location) {
        logger.log(Level.FINE, "Loading resource bundle: {0} ", new Object[]{path});
        PropertyResourceBundle bundle = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
            bundle = new PropertyResourceBundle(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if( inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bundle;
    }
}
