package ch.supsi.minesweeper.model;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Constant {
    public static final int GRID_HEIGHT = 9;
    public static final int GRID_WIDTH  = 9;
    public static final int CELL_COUNT  = GRID_HEIGHT * GRID_WIDTH;

    public static final int MAX_BOMBS_NUMBER = CELL_COUNT-1;

    // CONFIG
    public static final Path CONFIG_PATH = Paths.get(System.getProperty("user.home"), ".minesweeper", "preferences.properties");

    public static final String DEFAULT_LANGUAGE = "en";
    public static final int DEFAULT_BOMBS = 10;

    private Constant(){}
}
