package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.utility.UserPreferences;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;

public class GridModel extends AbstractModel {
    private static GridModel myself;
    private final static UserPreferences preferences = UserPreferences.getInstance();

    private transient boolean bombTriggered = false;

    @JsonProperty(required = true)
    private final int numberOfBombs;
    @JsonProperty(required = true)
    private int numberOfFlagsAvailable;
    @JsonProperty(required = true)
    private final CellModel[][] grid = new CellModel[Constant.GRID_HEIGHT][Constant.GRID_WIDTH];
    @JsonProperty(required = true)
    private int remainingCells;

    private GridModel(){
        int numberOfBombs;
        try{
            numberOfBombs = Integer.parseInt(preferences.getPreference("bombs"));
            if(numberOfBombs<=0 || numberOfBombs>=Constant.MAX_BOMBS_NUMBER)
                throw new NumberFormatException();
        }catch (NumberFormatException e){
            numberOfBombs = Constant.DEFAULT_BOMBS;
        }

        this.numberOfBombs = numberOfBombs;
        this.numberOfFlagsAvailable = numberOfBombs;
        this.remainingCells = Constant.CELL_COUNT - numberOfBombs;

        ArrayList<Boolean> bombsDistribution = new ArrayList<>();
        for(int i=0;i<numberOfBombs;i++)
            bombsDistribution.add(true);
        for(int i=0;i<Constant.CELL_COUNT-numberOfBombs;i++)
            bombsDistribution.add(false);
        Collections.shuffle(bombsDistribution);

        for(int i=0;i<Constant.GRID_HEIGHT;i++){
            for(int j=0;j<Constant.GRID_WIDTH;j++){
                grid[i][j] = new CellModel(bombsDistribution.get(i*Constant.GRID_WIDTH+j));
            }
        }
    }

    private boolean isCoordinatesValid(int row, int column){
        return row>=0 && row<Constant.GRID_HEIGHT && column>=0 && column<Constant.GRID_WIDTH;
    }

    public static GridModel getInstance(){
        if(myself==null) {
            myself = new GridModel();
        }
        return myself;
    }

    public void reset(){
        myself = new GridModel();
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
        CellEventHandler cell = grid[row][column];
        if(cell.hasFlag()) {
            cell.rightClick();
            numberOfFlagsAvailable++;
        }
        cell.leftClick();
        if(cell.hasBomb()){
            bombTriggered = true;
        }
        remainingCells--;
    }

    public void rightClick(int row, int column){
        if(!isCoordinatesValid(row,column))
            return;
        CellEventHandler cell = grid[row][column];
        if(!cell.isCovered())
            return;
        if(cell.hasFlag()){
            cell.rightClick();
            numberOfFlagsAvailable++;
            return;
        }
        if(numberOfFlagsAvailable == 0){
            return;
        }
        cell.rightClick();
        numberOfFlagsAvailable--;
    }

    public int getRemainingCells() {
        return remainingCells;
    }

    public int getNumberOfFlagsAvailable() {
        return numberOfFlagsAvailable;
    }
}
