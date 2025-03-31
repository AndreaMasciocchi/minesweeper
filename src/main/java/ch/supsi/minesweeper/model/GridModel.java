package ch.supsi.minesweeper.model;

public class GridModel {
    private final static int GRID_DIMENSION = 9;
    private int numberOfBombs;
    private int numberOfFlagsAvailable;
    private CellModel[][] grid = new CellModel[GRID_DIMENSION][GRID_DIMENSION];
}
