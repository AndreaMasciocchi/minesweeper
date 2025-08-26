package ch.supsi.minesweeper.model;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Constant {
    public static final int GRID_HEIGHT = 9;
    public static final int GRID_WIDTH  = 9;
    public static final int CELL_COUNT  = GRID_HEIGHT * GRID_WIDTH;

    // CONFIG
    public static final Path CONFIG_PATH = Paths.get(System.getProperty("user.home"), ".minesweeper", "preferences.properties");

    private Constant(){}
}
