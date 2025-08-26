package ch.supsi.minesweeper.utility;

public interface UserFeedbackListener {
    void showUserFeedback(String message, UserFeedbackType type, String... replacements);

    enum UserFeedbackType {
        INFO,
        SUCCESS,
        ERROR
    }
}
