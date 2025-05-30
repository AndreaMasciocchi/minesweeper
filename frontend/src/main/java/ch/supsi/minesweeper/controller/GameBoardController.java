package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.PlayerEventHandler;
import ch.supsi.minesweeper.view.DataView;

import java.util.List;

public class GameBoardController implements PlayerEventHandler {
    private static GameBoardController myself;
    private PlayerEventHandler handler;
    private List<DataView> views;
    private GameBoardController(){
        this.handler = GameModel.getInstance();
    }

    public static GameBoardController getInstance(){
        if(myself==null){
            myself = new GameBoardController();
        }
        return myself;
    }

    public void initialize(List<DataView> views) {
        this.views = views;
    }

    @Override
    public void move(int row, int column,boolean isLeftClick) {
        handler.move(row,column,isLeftClick);
        views.forEach(DataView::update);
    }
}
