package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.dataaccess.LanguageDAO;
import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameInformationHandler;
import ch.supsi.minesweeper.model.GameModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;

public class UserFeedbackViewFxml implements UncontrolledFxView {
    private static UserFeedbackViewFxml myself;
    private GameInformationHandler gameInformationHandler;
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
        this.gameInformationHandler = (GameModel) model;
        this.userFeedbackBar.setText(language.getString("label.userfeedbackbar.default"));
    }

    @Override
    public Node getNode() {
        return this.containerPane;
    }

    @Override
    public void update() {
        this.userFeedbackBar.setText(gameInformationHandler.getUserFeedback());
    }

}
