package ch.supsi.minesweeper.model;

public class CellModel implements CellEventHandler{
    private final boolean hasBomb;
    private boolean hasFlag = false;
    private boolean isCovered = true;

    CellModel(final boolean bomb){
        hasBomb = bomb;
    }

    @Override
    public void rightClick() {
        isCovered = !isCovered;
    }

    @Override
    public void leftClick() {
        hasFlag = !hasFlag;
    }
}
