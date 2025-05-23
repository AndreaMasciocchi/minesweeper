package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.PlayerEventHandler;
import ch.supsi.minesweeper.view.DataView;

import java.util.List;

public class BoardController implements PlayerEventHandler {
    private static BoardController myself;
    private PlayerEventHandler handler;
    private List<DataView> views;
    private BoardController(){
        this.handler = GameModel.getInstance();
    }

    public static BoardController getInstance(){
        if(myself==null){
            myself = new BoardController();
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
