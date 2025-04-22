package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.view.GameBoardViewFxml;
import ch.supsi.minesweeper.view.UserFeedbackViewFxml;

public class GameModel extends AbstractModel implements GameEventHandler, PlayerEventHandler{

    private static GameModel myself;
    private final GridModel grid = GridModel.getInstance();
    private final GameBoardViewFxml boardView = GameBoardViewFxml.getInstance();
    private final UserFeedbackViewFxml feedbackView = UserFeedbackViewFxml.getInstance();

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

    }

    @Override
    public void help() {

    }

    @Override
    public void about() {

    }
    public String getUserFeedback(){
        return grid.getFeedback();
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
        if(isLeftClick)
            grid.leftClick(row,column);
        else
            grid.rightClick(row,column);
        boardView.updateCell(row,column);
        feedbackView.update();
    }

    // add all the relevant missing behaviours
    // ...

}
