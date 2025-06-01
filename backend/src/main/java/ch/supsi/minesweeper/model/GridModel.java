package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.dataaccess.LanguageDAO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;

public class GridModel {
    private final static int GRID_DIMENSION = 9;
    private final static int MAX_BOMBS_NUMBER = GRID_DIMENSION*GRID_DIMENSION-1;
    private static GridModel myself;
    @JsonProperty(required = true)
    private final int numberOfBombs;
    @JsonProperty(required = true)
    private int numberOfFlagsAvailable;
    private transient String feedback;
    private final static UserPreferencesInterface preferences = UserPreferencesModel.getInstance();
    @JsonProperty(required = true)
    private final Cell[][] grid = new Cell[GRID_DIMENSION][GRID_DIMENSION];
    private transient boolean bombTriggered = false;
    @JsonProperty(required = true)
    private int remainingCells;
    private transient LanguageDAO language = LanguageDAO.getInstance();

    private GridModel(final int numberOfBombs){
        this.numberOfBombs = numberOfBombs;
        this.numberOfFlagsAvailable = numberOfBombs;
        this.remainingCells = GRID_DIMENSION*GRID_DIMENSION - numberOfBombs;

        ArrayList<Boolean> bombsDistribution = new ArrayList<>();
        for(int i=0;i<numberOfBombs;i++)
            bombsDistribution.add(true);
        for(int i=0;i<GRID_DIMENSION*GRID_DIMENSION-numberOfBombs;i++)
            bombsDistribution.add(false);
        Collections.shuffle(bombsDistribution);

        for(int i=0;i<GRID_DIMENSION;i++){
            for(int j=0;j<GRID_DIMENSION;j++){
                grid[i][j] = new CellModel(bombsDistribution.get(i*GRID_DIMENSION+j));
            }
        }
    }

    private boolean isCoordinatesValid(int row, int column){
        return row>=0 && row<GRID_DIMENSION && column>=0 && column<GRID_DIMENSION;
    }

    public static GridModel getInstance(){
        if(myself==null) {
            int numberOfBombs;
            try{
                numberOfBombs = Integer.parseInt(preferences.getPreferences("Mines"));
                if(numberOfBombs<=0 || numberOfBombs>=MAX_BOMBS_NUMBER)
                    throw new NumberFormatException();
            }catch (NumberFormatException e){
                numberOfBombs = Integer.parseInt(preferences.getDefaultPreferences("Mines"));
            }
            myself = new GridModel(numberOfBombs);
        }
        return myself;
    }
    public void reset(){
        myself = new GridModel(numberOfBombs);
    }
    public int getGridDimension(){
        return GRID_DIMENSION;
    }
    public String getFeedback(){
        return feedback;
    }
    public boolean isCellCovered(int row,int column){
        if(!isCoordinatesValid(row,column))
            return false;
        return grid[row][column].isCovered();
    }
    public boolean hasCellBomb(int row,int column){
        if(!isCoordinatesValid(row,column))
            return false;
        return grid[row][column].hasBomb();
    }
    public boolean isBombTriggered(){
        boolean triggered = bombTriggered;
        bombTriggered = false;
        return triggered;
    }
    public boolean isCellFlagged(int row,int column){
        if(!isCoordinatesValid(row,column))
            return false;
        return grid[row][column].hasFlag();
    }
    public int getNumberOfAdjacentBombs(int row, int column){
        if(!isCoordinatesValid(row,column))
            return -1;
        int count = 0;
        if(isCoordinatesValid(row+1,column) && grid[row+1][column].hasBomb())
            ++count;
        if(isCoordinatesValid(row-1,column) && grid[row-1][column].hasBomb())
            ++count;
        if(isCoordinatesValid(row,column+1) && grid[row][column+1].hasBomb())
            ++count;
        if(isCoordinatesValid(row,column-1) && grid[row][column-1].hasBomb())
            ++count;
        if(isCoordinatesValid(row-1,column+1) && grid[row-1][column+1].hasBomb())
            ++count;
        if(isCoordinatesValid(row+1,column+1) && grid[row+1][column+1].hasBomb())
            ++count;
        if(isCoordinatesValid(row+1,column-1) && grid[row+1][column-1].hasBomb())
            ++count;
        if(isCoordinatesValid(row-1,column-1) && grid[row-1][column-1].hasBomb())
            ++count;
        return count;
    }
    public void leftClick(int row, int column){
        if(!isCoordinatesValid(row,column))
            return;
        Cell cell = grid[row][column];
        feedback = "";
        if(cell.hasFlag()) {
            cell.rightClick();
            numberOfFlagsAvailable++;
            feedback = language.getString("label.feedback.flagremoved").replace("_", row+","+column+":"+numberOfFlagsAvailable);
        }
        cell.leftClick();
        if(cell.hasBomb()){
            feedback = language.getString("label.feedback.gameover");
            bombTriggered = true;
        }
        remainingCells--;
    }

    public void rightClick(int row, int column){
        if(!isCoordinatesValid(row,column))
            return;
        Cell cell = grid[row][column];
        if(!cell.isCovered())
            return;
        if(cell.hasFlag()){
            cell.rightClick();
            numberOfFlagsAvailable++;
            feedback = language.getString("label.feedback.flagremoved").replace("_", row+","+column+": "+numberOfFlagsAvailable);
            return;
        }
        if(numberOfFlagsAvailable == 0){
            feedback = language.getString("label.feedback.noflags");;
            return;
        }
        cell.rightClick();
        numberOfFlagsAvailable--;
        feedback = language.getString("label.feedback.flagplaced").replace("_", row+","+column+": "+numberOfFlagsAvailable);
    }

    public int getRemainingCells() {
        return remainingCells;
    }
}
