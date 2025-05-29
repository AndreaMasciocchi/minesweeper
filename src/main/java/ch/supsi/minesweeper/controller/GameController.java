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
        handler.newGame();
        views.forEach(DataView::update);
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

    @Override
    public void open() {
        handler.open();
        views.forEach(DataView::update);
    }

}
