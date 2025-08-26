package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.dataaccess.LanguageDAO;
import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameInformationHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.utility.UserFeedbackListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;

public class UserFeedbackViewFxml implements UncontrolledFxView, UserFeedbackListener {
    private static UserFeedbackViewFxml myself;
    private GameModel gameModel;
    @FXML
    private ScrollPane containerPane;
    @FXML
    private Text userFeedbackBar;
    private LanguageDAO language = LanguageDAO.getInstance();
    private UserFeedbackViewFxml() {}

    public static UserFeedbackViewFxml getInstance() {
        if (myself == null) {
            myself = new UserFeedbackViewFxml();

            try {
                URL fxmlUrl = UserFeedbackViewFxml.class.getResource("/userfeedbackbar.fxml");
                if (fxmlUrl != null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
                    fxmlLoader.setController(myself);
                    fxmlLoader.load();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return myself;
    }

    @Override
    public void initialize(AbstractModel model) {
        this.gameModel = (GameModel) model;
        gameModel.addUserFeedbackListener(this);
        this.userFeedbackBar.setText(language.getString("label.userfeedbackbar.default"));
    }

    @Override
    public Node getNode() {
        return this.containerPane;
    }

    @Override
    public void update() {
    }

    public void showUserFeedback(String messageKey, UserFeedbackType type, String... replacements) {
        Platform.runLater(() -> {
            // Recupero la stringa dal bundle
            String msg = language.getString(messageKey);

            // Applico i replacements agli underscore "_"
            for (String r : replacements) {
                msg = msg.replaceFirst("_", r);
            }

            // Mostro il messaggio finale
            userFeedbackBar.setText(msg);

            // Imposto il colore in base al tipo di feedback
            switch (type) {
                case SUCCESS -> userFeedbackBar.setStyle("-fx-fill: green;");
                case INFO    -> userFeedbackBar.setStyle("-fx-fill: blue;");
                case ERROR   -> userFeedbackBar.setStyle("-fx-fill: red;");
                default      -> userFeedbackBar.setStyle("-fx-fill: black;");
            }
        });
    }

}
