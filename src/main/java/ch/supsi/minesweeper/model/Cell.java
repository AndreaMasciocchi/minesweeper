package ch.supsi.minesweeper.model;

public interface Cell {
    void rightClick();
    void leftClick();
    boolean isCovered();
    boolean hasBomb();
    boolean hasFlag();
}
