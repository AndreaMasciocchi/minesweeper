package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.controller.EventHandler;

public interface CellEventHandler extends EventHandler {
    void rightClick();
    void leftClick();
}
