package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.dataaccess.SaveGameDAO;
import ch.supsi.minesweeper.view.GameBoardViewFxml;
import ch.supsi.minesweeper.view.MenuBarViewFxml;
import ch.supsi.minesweeper.view.UserFeedbackViewFxml;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonSyntaxException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;

public class GameModel extends AbstractModel implements GameEventHandler, PlayerEventHandler{

    private static GameModel myself;
    private GridModel grid = GridModel.getInstance();
    private final SaveGameDAO persistenceUtilities = SaveGameDAO.getInstance();
    private final GameBoardViewFxml boardView = GameBoardViewFxml.getInstance();
    private final UserFeedbackViewFxml feedbackView = UserFeedbackViewFxml.getInstance();
    private final MenuBarViewFxml menuView = MenuBarViewFxml.getInstance();
    private String feedback;

    private GameModel() {
        super();
    }

    public static GameModel getInstance() {
        if (myself == null) {
            myself = new GameModel();
        }

        return myself;
    }

    @Override
    public void newGame() {

    }

    @Override
    public void save() {
        try {
            persistenceUtilities.persist(grid);
        } catch (FileNotFoundException e) {
            setUserFeedback("Aborted: an error occurred while saving the game");
            feedbackView.update();
            return;
        }
        setUserFeedback("Game saved to "+persistenceUtilities.getLastSavedFileAbsolutePath());
        feedbackView.update();
        menuView.disableSave();
    }

    @Override
    public void saveAs() {
        FileDialog fileDialog = new FileDialog(new Frame(),"Save as",FileDialog.SAVE);
        fileDialog.setVisible(true);
        String directory = fileDialog.getDirectory();
        if(directory==null){
            setUserFeedback("Aborted: game not saved");
            feedbackView.update();
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
            feedbackView.update();
            return;
        }
        setUserFeedback("Game saved to "+persistenceUtilities.getLastSavedFileAbsolutePath());
        feedbackView.update();
        menuView.disableSave();
    }

    @Override
    public void help() {

    }

    @Override
    public void about() {

    }

    @Override
    public void open(final boolean isGameSaved) {
        if(!isGameSaved){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game not saved");
            alert.setHeaderText("Are you sure you want to start a new game without saving the current one?");
            alert.setContentText("If you confirm, all the progresses made in the current game will be definitively lost.");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get()==ButtonType.CANCEL)
                return;
        }
        FileDialog fileDialog = new FileDialog(new Frame(),"Choose file",FileDialog.LOAD);
        fileDialog.setVisible(true);
        String fileName = fileDialog.getFile();
        if(fileName==null){
            setUserFeedback("Aborted: no game loaded");
            feedbackView.update();
            return;
        }
        File file = new File(fileDialog.getDirectory()+File.separator+fileName);
        String json;
        try(Scanner reader = new Scanner(file)){
            StringBuilder sb = new StringBuilder();
            while(reader.hasNextLine())
                sb.append(reader.nextLine());
            json = sb.toString();
        }catch(FileNotFoundException e){
            setUserFeedback("Aborted: an error occurred while reading the file");
            feedbackView.update();
            return;
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(GridModel.class,(InstanceCreator<GridModel>) type -> GridModel.getInstance()).create();
        try{
            grid = gson.fromJson(json, GridModel.class);
        }catch(JsonSyntaxException e){
            setUserFeedback("Aborted: file corrupted or invalid format");
            feedbackView.update();
            return;
        }
        setUserFeedback("Game loaded from "+fileName);
        feedbackView.update();
        menuView.disableSave();
    }

    private void setUserFeedback(String msg){
        feedback = msg;
    }
    public String getUserFeedback(){
        return feedback;
    }
    public int getGridDimension(){
        return grid.getGridDimension();
    }
    public boolean isCellCovered(int row,int column){
        return grid.isCellCovered(row,column);
    }
    public boolean hasCellBomb(int row, int column){
        return grid.hasCellBomb(row,column);
    }
    public int getNumberOfAdjacentBombs(int row,int column){
        return grid.getNumberOfAdjacentBombs(row,column);
    }
    public boolean isCellFlagged(int row,int column){
        return grid.isCellFlagged(row,column);
    }
    @Override
    public void move(int row,int column, boolean isLeftClick) {
        menuView.enableSave();
        if(isLeftClick)
            grid.leftClick(row,column);
        else
            grid.rightClick(row,column);
        setUserFeedback(grid.getFeedback());
        boardView.updateCell(row,column);
        feedbackView.update();
    }

    // add all the relevant missing behaviours
    // ...

}
