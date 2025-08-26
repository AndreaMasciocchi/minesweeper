package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.Exceptions.FileProcessingException;
import ch.supsi.minesweeper.Exceptions.FileSyntaxException;
import ch.supsi.minesweeper.Exceptions.MalformedFileException;
import ch.supsi.minesweeper.dataaccess.*;
import ch.supsi.minesweeper.utility.UserActionListener;
import ch.supsi.minesweeper.utility.UserFeedbackListener;
import ch.supsi.minesweeper.utility.UserFeedbackListener.UserFeedbackType;
import ch.supsi.minesweeper.utility.UserPreferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GameModel extends AbstractModel implements GameEventHandler, PlayerEventHandler, GameInformationHandler{
    private static GameModel myself;
    private GridModel grid;
    private final DataPersistenceInterface persistenceUtilities = JsonPersistenceDAO.getInstance();
    private boolean gameOver = false;
    private boolean victory = false;
    private boolean gameSavable = false;
    private boolean gameNotStarted = true;
    private static UserPreferences userPreferences;

    private final List<UserFeedbackListener> feedbackListeners = new ArrayList<>();

    private GameModel() {
        super();
        userPreferences = UserPreferences.getInstance();
        grid = GridModel.getInstance();
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
        if(askToSave("label.newgame.ask")){
            return;
        }
        grid.reset();
        grid = GridModel.getInstance();
        setGameOver(false);
        setVictory(false);
        setGameSavable(false);
        setGameStarted();
        notifyUserFeedback("label.newgame.feedback", UserFeedbackType.INFO, String.valueOf((int)(Math.pow(Constant.GRID_WIDTH,2)) - grid.getRemainingCells()));
    }

    @Override
    public void save() {
        try {
            persistenceUtilities.persist(grid);
        } catch (FileNotFoundException e) {
            notifyUserFeedback("label.save.err1", UserFeedbackType.ERROR);
            return;
        }
        notifyUserFeedback("label.save", UserFeedbackType.SUCCESS, persistenceUtilities.getLastSavedFileAbsolutePath());
        setGameSavable(false);
    }

    public void saveAs(File file) {
        try {
            persistenceUtilities.persist(grid, file);
        } catch (FileNotFoundException e) {
            notifyUserFeedback("label.save.err1" , UserFeedbackType.ERROR);
            return;
        }
        notifyUserFeedback("label.save", UserFeedbackType.SUCCESS, persistenceUtilities.getLastSavedFileAbsolutePath());
        setGameSavable(false);
    }

    public void open(File file) {
        GridModel newGrid;
        try {
            newGrid = (GridModel) persistenceUtilities.deserialize(file, GridModel.class);
        } catch (FileNotFoundException e) {
            notifyUserFeedback("label.open.err2", UserFeedbackType.ERROR);
            return;
        } catch(FileSyntaxException | MalformedFileException e) {
            notifyUserFeedback("label.open.err3", UserFeedbackType.ERROR);
            return;
        } catch (FileProcessingException e) {
            notifyUserFeedback("label.open.err4", UserFeedbackType.ERROR);
            return;
        }
        grid = newGrid;
        setGameOver(false);
        setVictory(false);
        setGameSavable(false);
        notifyUserFeedback("label.open.loaded", UserFeedbackType.SUCCESS, file.getAbsolutePath());
    }

    @Override
    public void quit() {
        if(askToSave("label.askToSave.quit")){
            return;
        }
        javafx.application.Platform.exit();
        System.exit(0);
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
        else{
            grid.rightClick(row,column);
            if(grid.getNumberOfFlagsAvailable() != 0){
                notifyUserFeedback(grid.isCellFlagged(row,column) ? "label.feedback.flagplaced" : "label.feedback.flagremoved", UserFeedbackType.INFO, "("+row+";"+column+"), " + grid.getNumberOfFlagsAvailable());
            }else{
                notifyUserFeedback("label.feedback.noflags", UserFeedbackType.INFO);
            }
        }
        if(grid.isBombTriggered()) {
            setGameSavable(false);
            setGameOver(true);
            notifyUserFeedback("label.feedback.gameover", UserFeedbackType.ERROR);
            return;
        }
        if(grid.getRemainingCells()==0){
            setVictory(true);
            setGameSavable(false);
            notifyUserFeedback("label.win", UserFeedbackType.SUCCESS);
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

    public void addUserFeedbackListener(UserFeedbackListener listener) {
        feedbackListeners.add(listener);
    }

    public void removeUserFeedbackListener(UserFeedbackListener listener) {
        feedbackListeners.remove(listener);
    }

    public void notifyUserFeedback(String messageKey, UserFeedbackType type, String... replacement) {
        for (UserFeedbackListener listener : feedbackListeners) {
            listener.showUserFeedback(messageKey, type, replacement);
        }
    }

    private UserActionListener actionListener;

    public void setUserActionListener(UserActionListener listener) {
        this.actionListener = listener;
    }

    private boolean askToSave(String actionKey) {
        if (isGameSavable() && actionListener != null) {
            return !actionListener.askConfirmation("label.askToSave.header", actionKey);
        }
        return false;
    }

}
