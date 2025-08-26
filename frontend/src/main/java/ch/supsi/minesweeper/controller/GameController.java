package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.dataaccess.LanguageDAO;
import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.utility.UserFeedbackListener;
import ch.supsi.minesweeper.utility.UserFeedbackListener.UserFeedbackType;
import ch.supsi.minesweeper.view.DataView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class GameController implements GameEventHandler{
    private static GameController myself;
    private GameModel gameModel;
    private List<DataView> views;
    private LanguageDAO language = LanguageDAO.getInstance();

    private GameController () {
        this.gameModel = GameModel.getInstance();
        this.gameModel.setUserActionListener((messageKey, actionKey) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(language.getString("label.askToSave.title"));
            alert.setHeaderText(language.getString(messageKey).replace("_", language.getString(actionKey)));
            alert.setContentText(language.getString("label.askToSave.content"));
            Optional<ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.OK;
        });
    }

    public static GameController getInstance() {
        if (myself == null) {
            myself = new GameController();
        }

        return myself;
    }

    public void initialize(List<DataView> views) {
        this.views = views;
    }

    @Override
    public void newGame() {
        gameModel.newGame();
        views.forEach(DataView::update);
    }

    @Override
    public void save() {
        gameModel.save();
        views.forEach(DataView::update);
    }

    @Override
    public void saveAs(File file) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(language.getString("label.file.saveAs"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        file = fileChooser.showSaveDialog(null);

        if (file == null) {
            gameModel.notifyUserFeedback("label.save.err2", UserFeedbackListener.UserFeedbackType.ERROR);
            return;
        }

        gameModel.saveAs(file);

        views.forEach(DataView::update);
    }

    @Override
    public void open(File file) {
        if (askToSave("label.open.opengame")) {
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(language.getString("label.open.choosefile"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        file = fileChooser.showOpenDialog(null);

        if (file == null) {
            gameModel.notifyUserFeedback("label.open.err1", UserFeedbackType.ERROR);
            return;
        }

        gameModel.open(file);

        views.forEach(DataView::update);
    }

    @Override
    public void quit() {
        gameModel.quit();
        views.forEach(DataView::update);
    }

    private boolean askToSave(String actionKey) {
        if (!gameModel.isGameSavable()) return false;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(language.getString("label.askToSave.title"));
        alert.setHeaderText(language.getString("label.askToSave.header").replace("_", language.getString(actionKey)));
        alert.setContentText(language.getString("label.askToSave.content"));

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.CANCEL;
    }
}
