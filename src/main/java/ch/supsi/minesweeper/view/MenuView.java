package ch.supsi.minesweeper.view;

public interface MenuView extends ControlledFxView{
    void setEnableSave(boolean flag);
    boolean isSaveDisabled();
}
