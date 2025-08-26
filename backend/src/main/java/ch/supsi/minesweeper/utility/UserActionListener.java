package ch.supsi.minesweeper.utility;

public interface UserActionListener {
    boolean askConfirmation(String messageKey, String replacement);
}

