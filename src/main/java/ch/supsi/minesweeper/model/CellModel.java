package ch.supsi.minesweeper.model;

public class CellModel implements CellEventHandler{
    private final boolean hasBomb;
    private boolean hasFlag = false;
    private boolean isCovered = true;

    CellModel(final boolean bomb){
        hasBomb = bomb;
    }

    public boolean isCovered(){
        return isCovered;
    }

    public boolean hasFlag(){
        return hasFlag;
    }

    public boolean hasBomb() {
        return hasBomb;
    }

    @Override
    public void rightClick() {
        hasFlag = !hasFlag;
    }

    @Override
    public void leftClick() {
        isCovered = false;
    }
}
