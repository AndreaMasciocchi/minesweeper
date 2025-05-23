package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.view.DataView;
import javafx.scene.control.Alert;

import java.util.List;

public class GameController implements GameEventHandler{

    private static GameController myself;

    private GameEventHandler handler;
    private List<DataView> views;

    private GameController () {
        this.handler = GameModel.getInstance();
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
        handler.save();
        views.forEach(DataView::update);
    }

    @Override
    public void saveAs() {
        handler.saveAs();
        views.forEach(DataView::update);
    }

    // add all the relevant methods to handle all those defined by the GameEventHandler interface
    // ...

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
        handler.open(isGameSaved);
        views.forEach(DataView::update);
    }

}
