package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.controller.EventHandler;
import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.PlayerEventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.Annotation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GameBoardViewFxml implements ControlledFxView {

    private static GameBoardViewFxml myself;

    private PlayerEventHandler playerEventHandler;

    private GameModel gameModel;

    @FXML
    private GridPane containerPane;

    @FXML
    private Button cell00;

    @FXML
    private Button cell01;

    @FXML
    private Button cell02;

    @FXML
    private Button cell03;

    @FXML
    private Button cell04;

    @FXML
    private Button cell05;

    @FXML
    private Button cell06;

    @FXML
    private Button cell07;

    @FXML
    private Button cell08;

    @FXML
    private Button cell10;

    @FXML
    private Button cell11;

    @FXML
    private Button cell12;

    @FXML
    private Button cell13;

    @FXML
    private Button cell14;

    @FXML
    private Button cell15;

    @FXML
    private Button cell16;

    @FXML
    private Button cell17;

    @FXML
    private Button cell18;

    @FXML
    private Button cell20;

    @FXML
    private Button cell21;

    @FXML
    private Button cell22;

    @FXML
    private Button cell23;

    @FXML
    private Button cell24;

    @FXML
    private Button cell25;

    @FXML
    private Button cell26;

    @FXML
    private Button cell27;

    @FXML
    private Button cell28;

    @FXML
    private Button cell30;

    @FXML
    private Button cell31;

    @FXML
    private Button cell32;

    @FXML
    private Button cell33;

    @FXML
    private Button cell34;

    @FXML
    private Button cell35;

    @FXML
    private Button cell36;

    @FXML
    private Button cell37;

    @FXML
    private Button cell38;

    @FXML
    private Button cell40;

    @FXML
    private Button cell41;

    @FXML
    private Button cell42;

    @FXML
    private Button cell43;

    @FXML
    private Button cell44;

    @FXML
    private Button cell45;

    @FXML
    private Button cell46;

    @FXML
    private Button cell47;

    @FXML
    private Button cell48;

    @FXML
    private Button cell50;

    @FXML
    private Button cell51;

    @FXML
    private Button cell52;

    @FXML
    private Button cell53;

    @FXML
    private Button cell54;

    @FXML
    private Button cell55;

    @FXML
    private Button cell56;

    @FXML
    private Button cell57;

    @FXML
    private Button cell58;

    @FXML
    private Button cell60;

    @FXML
    private Button cell61;

    @FXML
    private Button cell62;

    @FXML
    private Button cell63;

    @FXML
    private Button cell64;

    @FXML
    private Button cell65;

    @FXML
    private Button cell66;

    @FXML
    private Button cell67;

    @FXML
    private Button cell68;

    @FXML
    private Button cell70;

    @FXML
    private Button cell71;

    @FXML
    private Button cell72;

    @FXML
    private Button cell73;

    @FXML
    private Button cell74;

    @FXML
    private Button cell75;

    @FXML
    private Button cell76;

    @FXML
    private Button cell77;

    @FXML
    private Button cell78;

    @FXML
    private Button cell80;

    @FXML
    private Button cell81;

    @FXML
    private Button cell82;

    @FXML
    private Button cell83;

    @FXML
    private Button cell84;

    @FXML
    private Button cell85;

    @FXML
    private Button cell86;

    @FXML
    private Button cell87;
    @FXML
    private Button cell88;
    private final ArrayList<Button> buttons = new ArrayList<>();
    private GameBoardViewFxml() {
    }
    private void initializeButtonsList(){
        for(Field f : GameBoardViewFxml.class.getDeclaredFields()){
            if(f.isAnnotationPresent(FXML.class) && f.getType().equals(Button.class)){
                f.setAccessible(true);
                try {
                    buttons.add((Button) f.get(this));
                } catch (IllegalAccessException e) {
                    //
                }
            }
        }
    }

    public static GameBoardViewFxml getInstance() {
        if (myself == null) {
            myself = new GameBoardViewFxml();
            try {
                URL fxmlUrl = GameBoardViewFxml.class.getResource("/gameboard.fxml");
                if (fxmlUrl != null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
                    fxmlLoader.setController(myself);
                    fxmlLoader.load();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            myself.initializeButtonsList();
        }

        return myself;
    }

    @Override
    public void initialize(EventHandler eventHandler, AbstractModel model) {
        this.createBehaviour();
        this.playerEventHandler = (PlayerEventHandler) eventHandler;
        this.gameModel = (GameModel) model;
    }

    private void createBehaviour() {
        for(Button button : buttons){
            button.setOnMouseClicked(event->this.playerEventHandler.move(button.getId().charAt(4)-'0',button.getId().charAt(5)-'0',event.getButton()==MouseButton.PRIMARY));
        }
    }

    @Override
    public Node getNode() {
        return this.containerPane;
    }

    //TODO: split in two different methods (single responsibility principle)
    private void checkCellStatusAndSetButtonText(int row, int column){
        int dimension = gameModel.getGridDimension();
        Button button = buttons.get(row*dimension+column);
        if(!gameModel.isCellCovered(row,column)){
            if(gameModel.hasCellBomb(row,column)) {
                button.setText("\uD83D\uDCA3");
                for(Button b : buttons)
                    b.setDisable(true);
            }
            else
                button.setText(String.valueOf(gameModel.getNumberOfAdjacentBombs(row,column)));
        }else if(gameModel.isCellFlagged(row,column))
            button.setText(new String(Character.toChars(0x2691)));
        else
            button.setText(button.getId().charAt(4)+","+button.getId().charAt(5));
    }

    @Override
    public void update() {
        // get your data from the model, if needed
        // then update this view here

        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //Date date = new Date(System.currentTimeMillis());
        //System.out.println(this.getClass().getSimpleName() + " updated..." + dateFormat.format(date));

        int dimension = gameModel.getGridDimension();
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                checkCellStatusAndSetButtonText(i,j);
            }
        }
        System.out.println(this.getClass().getSimpleName() + " updated..." + System.currentTimeMillis());
    }

    public void updateCell(int row, int column){
        int dimension = gameModel.getGridDimension();
        if(row < 0 || row >= dimension || column < 0 || column >= dimension)
            return;
        checkCellStatusAndSetButtonText(row,column);
        System.out.println(this.getClass().getSimpleName() + " updated..." + System.currentTimeMillis());
    }
}
