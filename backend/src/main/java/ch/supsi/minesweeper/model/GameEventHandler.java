package ch.supsi.minesweeper.model;


import java.io.File;

public interface GameEventHandler extends EventHandler {

    void newGame();

    void save();

    void saveAs(File file);

    void open(File file);

    void quit();
}
