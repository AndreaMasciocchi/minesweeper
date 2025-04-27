package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.GridModel;
import ch.supsi.minesweeper.model.PlayerEventHandler;
import ch.supsi.minesweeper.view.DataView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;

public class GameController implements GameEventHandler, PlayerEventHandler {

    private static GameController myself;

    private GameModel gameModel;
    private List<DataView> views;

    private GameController () {
        this.gameModel = GameModel.getInstance();
    }

    public static GameController getInstance() {
        if (myself == null) {
            myself = new GameController();
        }

        return myself;
    }

    public void initialize(List<DataView> views) {
        this.views = views;
    }

    @Override
    public void newGame() {
        // do whatever you must do to start a new game

        // then update your views
        this.views.forEach(DataView::update);
    }

    @Override
    public void save() {
        gameModel.save();
    }

    @Override
    public void saveAs() {
        gameModel.saveAs();
    }

    // add all the relevant methods to handle all those defined by the GameEventHandler interface
    // ...

    @Override
    public void move(int row, int column,boolean isLeftClick) {
        this.gameModel.move(row,column,isLeftClick);
        //views.forEach(DataView::update);
    }

    @Override
    public void help() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Come si gioca?");
        alert.setContentText("Ogni turno devi selezionare una casella\n Se fai esplodere una bomba hai perso");
        alert.showAndWait();
    }

    @Override
    public void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Informazioni sull'app");
        alert.setContentText("Minesweeper\nVersione: 1.0.0\nAutori: Mongillo, Masciocchi, Aliprandi");
        alert.showAndWait();
    }

    @Override
    public void open(final boolean isGameSaved) {
        gameModel.open(isGameSaved);
        views.forEach(DataView::update);
    }

}
