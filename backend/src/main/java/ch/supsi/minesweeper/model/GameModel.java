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
    private boolean victory = false;
    private boolean gameSavable = false;
    private boolean gameNotStarted = true;

    private LanguageDAO language = LanguageDAO.getInstance();

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
    private void setGameOver(final boolean flag){
        gameOver = flag;
    }
    private void setVictory(final boolean flag){
        victory = flag;
    }
    private void setGameStarted(){
        gameNotStarted = false;
    }

    @Override
    public boolean isGameStarted(){
        return !gameNotStarted;
    }

    @Override
    public boolean isGameSavable(){
        return gameSavable;
    }

    @Override
    public void newGame() {
        if (askToSave(language.getString("label.newgame.ask"))) return;
        grid.reset();
        grid = GridModel.getInstance();
        setGameOver(false);
        setVictory(false);
        setGameSavable(false);
        setGameStarted();
        setUserFeedback(language.getString("label.newgame.feedback").replace("_",String.valueOf((int)(Math.pow(grid.getGridDimension(),2)) - grid.getRemainingCells())));
    }

    private boolean askToSave(String action) {
        if(isGameSavable()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(language.getString("label.askToSave.title"));
            alert.setHeaderText(language.getString("label.askToSave.header").replace("_", action));
            alert.setContentText(language.getString("label.askToSave.content"));
            Optional<ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.CANCEL;
        }
        return false;
    }

    @Override
    public void save() {
        try {
            persistenceUtilities.persist(grid);
        } catch (FileNotFoundException e) {
            setUserFeedback(language.getString("label.save.err1"));
            return;
        }
        setUserFeedback(language.getString("label.save").replace("_",persistenceUtilities.getLastSavedFileAbsolutePath()));
        setGameSavable(false);
    }

    @Override
    public void saveAs() {
        FileDialog fileDialog = new FileDialog(new Frame(),language.getString("label.file.saveAs"),FileDialog.SAVE);
        fileDialog.setVisible(true);
        String directory = fileDialog.getDirectory();
        if(directory==null){
            setUserFeedback(language.getString("label.save.err2"));
            return;
        }
        String name = fileDialog.getFile();
        File file = new File(directory+File.separator+name);
        if(!file.exists() && !name.endsWith(".json"))
            file = new File(directory+File.separator+name+".json");
        try {
            persistenceUtilities.persist(grid,file);
        } catch (FileNotFoundException e) {
            setUserFeedback(language.getString("label.save.err1"));
            return;
        }
        setUserFeedback(language.getString("label.save")+" "+persistenceUtilities.getLastSavedFileAbsolutePath());
        setGameSavable(false);
    }

    @Override
    public void open() {
        if (askToSave(language.getString("label.open.opengame"))) return;
        FileDialog fileDialog = new FileDialog(new Frame(),language.getString("label.open.choosefile"),FileDialog.LOAD);
        fileDialog.setVisible(true);
        String fileName = fileDialog.getFile();
        if(fileName==null){
            setUserFeedback(language.getString("label.open.err1"));
            return;
        }
        String dir = fileDialog.getDirectory();
        File file = new File(dir+File.separator+fileName);
        GridModel newGrid;
        try {
            newGrid = (GridModel) persistenceUtilities.deserialize(file, GridModel.class);
        }catch (FileNotFoundException e){
            setUserFeedback(language.getString("label.open.err2"));
            return;
        }catch(FileSyntaxException | MalformedFileException e){
            setUserFeedback(language.getString("label.open.err3"));
            return;
        }catch (FileProcessingException e){
            setUserFeedback(language.getString("label.open.err4"));
            return;
        }
        grid = newGrid;
        setGameOver(false);
        setVictory(false);
        setGameSavable(false);
        setGameStarted();
        setUserFeedback(language.getString("label.open.loaded").replace("_",file.getAbsolutePath()));
    }

    @Override
    public void quit() {
        if(askToSave(language.getString("label.askToSave.quit"))) return;
        javafx.application.Platform.exit();
        System.exit(0);
    }

    private void setUserFeedback(String msg){
        feedback = msg;
    }

    @Override
    public String getUserFeedback() {
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
    public boolean isVictory() {
        return victory;
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
            setGameOver(true);
            return;
        }
        if(grid.getRemainingCells()==0){
            setVictory(true);
            setGameSavable(false);
            setUserFeedback(language.getString("label.win"));
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
