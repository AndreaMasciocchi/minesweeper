package ch.supsi.minesweeper.model;

public interface CellEventHandler extends EventHandler{
    void rightClick();
    void leftClick();
    boolean isCovered();
    boolean hasBomb();
    boolean hasFlag();
}
