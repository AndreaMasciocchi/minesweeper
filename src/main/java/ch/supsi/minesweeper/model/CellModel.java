package ch.supsi.minesweeper.model;

public class CellModel implements CellInterface{
    private final boolean hasBomb;
    private boolean hasFlag = false;
    private boolean isCovered = true;

    CellModel(final boolean bomb){
        hasBomb = bomb;
    }

    @Override
    public boolean isCovered(){
        return isCovered;
    }

    @Override
    public boolean hasFlag(){
        return hasFlag;
    }

    @Override
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
