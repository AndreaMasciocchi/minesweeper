package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.controller.EventHandler;

public interface PlayerEventHandler extends EventHandler {

    void move(int row,int column, boolean isLeftClick);

    // add all the relevant missing behaviours
    // ...

}
