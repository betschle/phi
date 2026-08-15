package com.neutronio.phi.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.neutronio.phi.PhiException;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Allows for saving and loading files compatible with GDX {@link com.badlogic.gdx.Files}
 * and GDX asset paths.
 */
public class GDXFileHandler implements FileHandler {

    // TODO write tests in gdx
    public static final String REGEX_PATTERN = "^[A-Za-z0-9._ ]{1,100}$";
    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    public static FileHandle toGdxFileHandle(String path, FileLocation fileLocation) {
        if (Gdx.files == null) {
            throw new PhiException(PhiException.ErrorCode.E0001, "LibGdx");
        }
        switch (fileLocation) {
            case INTERNAL: return Gdx.files.internal(path);
            case CLASSPATH: return Gdx.files.classpath(path);
            case EXTERNAL: return Gdx.files.external(path);
            case ABSOLUTE: return Gdx.files.absolute(path);
            case LOCAL: return Gdx.files.local(path);
        }
        return null;
    }

    public static class PhiFile implements Serializable {
        private static final long serialVersionUID = -4299132363679097821L;
        public transient File file;
        public String path;
        public FileLocation location;

        public PhiFile(File file, String path, FileLocation location) {
            this.file = file;
            this.path = path;
            this.location = location;
        }
    }

    @Override
    public boolean deleteFile(PhiFile file) {
        if( file == null) return false;
        logger.fine("Delete File: " + file.path );
        boolean delete = file.file.delete();
        // TODO Keep this in code until further notice, see when it triggers. For fixing #481
        if( delete && file.file.exists() ) {
            throw new PhiException(PhiException.ErrorCode.E0001, "File was allegedly deleted but still exists?!");
        }
        return delete;
    }

    /**
     *
     * @param path the path to file
     * @param location defines the location. Only matters if GDX was initialized.
     * @return when GDX was not initialized, returns an InputStream from source root
     */
    protected InputStream getReader(String path, FileLocation location) throws GdxRuntimeException {
        if( Gdx.files == null) { // fallback
            try { // TODO clumsy
                return new FileInputStream(path.replace('\\', '/'));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            return toGdxFileHandle(path, location).read();
        }
        return null;
    }

    @Override
    public List<PhiFile> readFilesInDirectory(String path, FileLocation location, String glob) throws IOException, GdxRuntimeException {
        FileHandle fileHandle = toGdxFileHandle(path, location);
        DirectoryStream<Path> dirs = Files.newDirectoryStream(fileHandle.file().toPath(), glob);
        List<PhiFile> directories = new ArrayList<>();
        Iterator<Path> iterator = dirs.iterator();
        while( iterator.hasNext()  ) {
            Path directory = iterator.next();
            directories.add( new PhiFile(directory.toFile(), path + "/"+directory.getFileName() , location) );
        }
        dirs.close();
        return directories;
    }

    @Override
    public StringBuilder readFileContents(String path, FileLocation location) throws IOException, GdxRuntimeException {
        InputStream stream = getReader(path, location);
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String currentLine = "";
        while ((currentLine = reader.readLine()) != null) {
            builder.append(currentLine);
        }
        reader.close();
        return builder;
    }

    @Override
    public State saveFileContents(String data, String path, FileLocation location) throws IOException {
        return saveFileContents(data, path, location, false);
    }

    @Override
    public State saveFileContents(String data, String path, FileLocation location, boolean overwrite) throws IOException {
        FileHandle fileHandle = toGdxFileHandle(path, location);
        File file = fileHandle.file();
        State state = null;
        if( !file.exists() ) {
            file.createNewFile();
            this.save(file, data);
            state = State.SAVED;
        } else if( overwrite ) {
            this.save(file, data);
            state = State.SAVED;
        } else {
            state = State.ABORTED;
        }
        return state;
    }

    // TODO test this
    // TODO valid file locations are: ABSOLUTE, LOCAL, EXTERNAL (, DATAPACK)
    @Override
    public State saveFileBinary(Serializable savable, String path, FileLocation location) {
        FileHandle fileHandle = toGdxFileHandle(path, location);
        FileOutputStream fileOutputStream = null;
        State state = null;
        try {
            fileOutputStream = new FileOutputStream(fileHandle.path());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(savable);
            objectOutputStream.flush();
            objectOutputStream.close();
            state = State.SAVED;
        } catch (IOException e) {
            e.printStackTrace();
            state = State.FAILED;
        }
        return state;
    }

    // TODO test this
    // TODO error should be thrown here instead of return Message
    public State saveFileBinary(Serializable savable, String fileName, FileHandle parentDirectory) {
        FileOutputStream fileOutputStream = null;
        State state = null;
        try {
            parentDirectory.mkdirs();
            fileOutputStream = new FileOutputStream(parentDirectory.child(fileName).path());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(savable);
            objectOutputStream.flush();
            objectOutputStream.close();
            state = State.SAVED;
        } catch (IOException e) {
            e.printStackTrace();
            state = State.FAILED;
        }
        return state;
    }

    // TODO test this
    public <T extends Serializable> T readFileBinary(String fileName, FileHandle parentDirectory) {
        FileInputStream fileInputStream = null;
        T loaded = null;
        try {
            fileInputStream = new FileInputStream(parentDirectory.child(fileName).path());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            loaded = (T) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loaded;
    }

    // TODO make this work with gdx files
    // TODO valid file locations are: ABSOLUTE, LOCAL, EXTERNAL (, DATAPACK)
    @Override
    public <T extends Serializable> T readFileBinary(String path, FileLocation location) {
        throw new UnsupportedOperationException("Not supported yet!");
    }

    private void save(File file, String data) throws IOException {
        BufferedWriter writer = null;
        writer = new BufferedWriter(new FileWriter(file));
        writer.write(data);

        if( writer != null)
            writer.close();
    }
}
