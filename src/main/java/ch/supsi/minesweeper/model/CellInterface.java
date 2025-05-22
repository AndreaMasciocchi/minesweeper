package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.controller.EventHandler;

public interface CellInterface{
    void rightClick();
    void leftClick();
    boolean isCovered();
    boolean hasBomb();
    boolean hasFlag();
}
