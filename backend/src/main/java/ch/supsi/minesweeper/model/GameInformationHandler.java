package ch.supsi.minesweeper.model;

public interface GameInformationHandler{
    boolean isCellCovered(final int row, final int column);
    boolean hasCellBomb(final int row, final int column);
    int getNumberOfAdjacentBombs(final int row,final int column);
    boolean isCellFlagged(final int row,final int column);
    boolean isGameOver();
    boolean isVictory();
    boolean isGameSavable();
    boolean isGameStarted();
}
