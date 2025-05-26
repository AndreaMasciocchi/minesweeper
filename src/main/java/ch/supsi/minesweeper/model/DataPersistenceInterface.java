package ch.supsi.minesweeper.model;

import java.io.File;
import java.io.FileNotFoundException;

public interface DataPersistenceInterface {
    void persist(Object o) throws FileNotFoundException;
    void persist(Object o, File file) throws FileNotFoundException;
    String getLastSavedFileAbsolutePath();
}
