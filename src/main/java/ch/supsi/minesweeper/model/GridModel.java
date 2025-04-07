package ch.supsi.minesweeper.model;

public class GridModel {
    private final static int GRID_DIMENSION = 9;

    private static GridModel myself;
    private final int numberOfBombs;
    private int numberOfFlagsAvailable;
    private CellModel[][] grid = new CellModel[GRID_DIMENSION][GRID_DIMENSION];

    private GridModel(final int numberOfBombs){
        this.numberOfBombs = numberOfBombs;
        this.numberOfFlagsAvailable = numberOfBombs;
    }

    public GridModel getInstance(){
        if(myself==null) {
            int numberOfBombs=0;//read number of bombs from UserPreferencesModel
            myself = new GridModel(numberOfBombs);
        }
        return myself;
    }
}
