package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.Exceptions.FileProcessingException;
import ch.supsi.minesweeper.Exceptions.FileSyntaxException;
import ch.supsi.minesweeper.Exceptions.MalformedFileException;
import ch.supsi.minesweeper.dataaccess.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

public class GameModel extends AbstractModel implements GameEventHandler, PlayerEventHandler, GameInformationHandler{
    private static GameModel myself;
    private GridModel grid = GridModel.getInstance();
    private final DataPersistenceInterface persistenceUtilities = JsonPersistenceDAO.getInstance();
    private String feedback;
    private boolean gameOver = false;
    private boolean gameSavable = false;

    private GameModel() {
        super();
    }

    public static GameModel getInstance() {
        if (myself == null) {
            myself = new GameModel();
        }

        return myself;
    }

    private void setGameSavable(final boolean flag){
        gameSavable = flag;
    }

    public boolean isGameSavable(){
        return gameSavable;
    }

    @Override
    public void newGame() {
        if(isGameSavable()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game not saved");
            alert.setHeaderText("Are you sure you want to start a new game without saving the current one?");
            alert.setContentText("All the progresses made in the current game will be definitively lost.");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get()==ButtonType.CANCEL)
                return;
        }
        if(!isGameOver()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Start new game");
            alert.setHeaderText("Are you sure you want to start a new game?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get()==ButtonType.CANCEL)
                return;
        }
        grid.reset();
        grid = GridModel.getInstance();
        gameOver = false;
        setUserFeedback("New game started!");
    }

    @Override
    public void save() {
        try {
            persistenceUtilities.persist(grid);
        } catch (FileNotFoundException e) {
            setUserFeedback("Aborted: an error occurred while saving the game");
            return;
        }
        setUserFeedback("Game saved to "+persistenceUtilities.getLastSavedFileAbsolutePath());
        setGameSavable(false);
    }

    @Override
    public void saveAs() {
        FileDialog fileDialog = new FileDialog(new Frame(),"Save as",FileDialog.SAVE);
        fileDialog.setVisible(true);
        String directory = fileDialog.getDirectory();
        if(directory==null){
            setUserFeedback("Aborted: game not saved");
            return;
        }
        String name = fileDialog.getFile();
        File file = new File(directory+File.separator+name);
        if(!file.exists() && !name.endsWith(".json"))
            file = new File(directory+File.separator+name+".json");
        try {
            persistenceUtilities.persist(grid,file);
        } catch (FileNotFoundException e) {
            setUserFeedback("Aborted: an error occurred while saving the game");
            return;
        }
        setUserFeedback("Game saved to "+persistenceUtilities.getLastSavedFileAbsolutePath());
        setGameSavable(false);
    }

    @Override
    public void open() {
        if(isGameSavable()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game not saved");
            alert.setHeaderText("Are you sure you want to start a new game without saving the current one?");
            alert.setContentText("All the progresses made in the current game will be definitively lost.");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get()==ButtonType.CANCEL)
                return;
        }
        FileDialog fileDialog = new FileDialog(new Frame(),"Choose file",FileDialog.LOAD);
        fileDialog.setVisible(true);
        String fileName = fileDialog.getFile();
        if(fileName==null){
            setUserFeedback("Aborted: no game loaded");
            return;
        }
        String dir = fileDialog.getDirectory();
        File file = new File(dir+File.separator+fileName);
        GridModel newGrid;
        try {
            newGrid = (GridModel) persistenceUtilities.deserialize(file, GridModel.class);
        }catch (FileNotFoundException e){
            setUserFeedback("Aborted: an error occurred while reading the file");
            return;
        }catch(FileSyntaxException | MalformedFileException e){
            setUserFeedback("Aborted: file corrupted");
            return;
        }catch (FileProcessingException e){
            setUserFeedback("Aborted: error parsing the file");
            return;
        }
        grid = newGrid;
        setUserFeedback("Game loaded from "+dir+fileName);
        setGameSavable(false);
    }
    private void setUserFeedback(String msg){
        feedback = msg;
    }
    @Override
    public String getUserFeedback(){
        return feedback;
    }
    @Override
    public int getGridDimension(){
        return grid.getGridDimension();
    }
    @Override
    public boolean isCellCovered(int row,int column){
        return grid.isCellCovered(row,column);
    }
    @Override
    public boolean hasCellBomb(int row, int column){
        return grid.hasCellBomb(row,column);
    }
    @Override
    public int getNumberOfAdjacentBombs(int row,int column){
        return grid.getNumberOfAdjacentBombs(row,column);
    }
    @Override
    public boolean isCellFlagged(int row,int column){
        return grid.isCellFlagged(row,column);
    }
    @Override
    public boolean isGameOver(){
        return gameOver;
    }
    @Override
    public void move(int row,int column, boolean isLeftClick) {
        setGameSavable(true);
        if(isLeftClick) {
            grid.leftClick(row, column);
            if (getNumberOfAdjacentBombs(row,column)==0) {
                uncoverEmptyAdjacentCells(row,column);
            }
        }
        else
            grid.rightClick(row,column);
        setUserFeedback(grid.getFeedback());
        if(grid.isBombTriggered()) {
            setGameSavable(false);
            gameOver = true;
        }
    }

    private void uncoverEmptyAdjacentCells(final int row, final int column){
        if(isCellCovered(row-1,column) && getNumberOfAdjacentBombs(row-1,column)==0)
            move(row-1,column,true);
        if(isCellCovered(row,column-1) && getNumberOfAdjacentBombs(row,column-1)==0)
            move(row,column-1,true);
        if(isCellCovered(row-1,column-1) && getNumberOfAdjacentBombs(row-1,column-1)==0)
            move(row-1,column-1,true);
        if(isCellCovered(row+1,column) && getNumberOfAdjacentBombs(row+1,column)==0)
            move(row+1,column,true);
        if(isCellCovered(row,column+1) && getNumberOfAdjacentBombs(row,column+1)==0)
            move(row,column+1,true);
        if(isCellCovered(row+1,column+1) && getNumberOfAdjacentBombs(row+1,column+1)==0)
            move(row+1,column+1,true);
    }
}
