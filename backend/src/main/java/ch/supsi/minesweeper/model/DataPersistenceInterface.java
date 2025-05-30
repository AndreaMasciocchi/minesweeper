package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.Exceptions.FileProcessingException;
import ch.supsi.minesweeper.Exceptions.FileSyntaxException;
import ch.supsi.minesweeper.Exceptions.MalformedFileException;

import java.io.File;
import java.io.FileNotFoundException;

public interface DataPersistenceInterface {
    void persist(Object o) throws FileNotFoundException;
    void persist(Object o, File file) throws FileNotFoundException;
    Object deserialize(File file, Class<?> clazz) throws MalformedFileException, FileProcessingException, FileSyntaxException, FileNotFoundException;
    String getLastSavedFileAbsolutePath();
}
