package ch.supsi.minesweeper.dataaccess;

import ch.supsi.minesweeper.model.DataPersistenceInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class PersistenceDAO implements DataPersistenceInterface {
    private static final String userHomeDirectory = System.getProperty("user.home");
    private static final String savedGamesDirectory = ".minesweeper"+ File.separator+"savings";
    protected Path savingsPath;
    protected PersistenceDAO(){
        try {
            savingsPath = Files.createDirectories(Path.of(userHomeDirectory,savedGamesDirectory));
        } catch (IOException e) {
            //
        }
    }
}
