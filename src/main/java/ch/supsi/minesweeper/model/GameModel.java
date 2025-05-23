package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.dataaccess.SaveGameDAO;
import ch.supsi.minesweeper.view.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.stream.MalformedJsonException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class GameModel extends AbstractModel implements GameEventHandler, PlayerEventHandler{

    private static GameModel myself;
    private GridModel grid = GridModel.getInstance();
    private final SaveGameDAO persistenceUtilities = SaveGameDAO.getInstance();
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
        grid.reset();
        grid = GridModel.getInstance();
        gameOver = false;
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
    public void help() {

    }

    @Override
    public void about() {

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
        String json;
        try(Scanner reader = new Scanner(file)){
            StringBuilder sb = new StringBuilder();
            while(reader.hasNextLine())
                sb.append(reader.nextLine());
            json = sb.toString();
        }catch(FileNotFoundException e){
            setUserFeedback("Aborted: an error occurred while reading the file");
            return;
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(GridModel.class,(InstanceCreator<GridModel>) type -> GridModel.getInstance()).create();
        try{
            new JSONObject(json);
        }catch (JSONException e){
            setUserFeedback("Aborted: invalid file format");
            return;
        }
        JsonValidator validator = new JsonValidator();
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);
            if(!validator.isJsonValid(jsonNode,validator.getJsonSchema(GridModel.class)))
                throw new MalformedJsonException("");
            grid = gson.fromJson(json, GridModel.class);
        }catch(JsonSyntaxException | MalformedJsonException e){
            setUserFeedback("Aborted: file corrupted");
            return;
        } catch (JsonProcessingException e) {
            setUserFeedback("Aborted: error parsing json");
            return;
        }
        setUserFeedback("Game loaded from "+dir+fileName);
        setGameSavable(false);
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
