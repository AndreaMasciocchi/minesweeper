package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.controller.EventHandler;

public interface GameEventHandler extends EventHandler {

    void newGame();

    void save();

    void saveAs();

    void help();

    void about();

    void open(final boolean isGameSaved);

    // add all the relevant missing behaviours
    // ...

}
