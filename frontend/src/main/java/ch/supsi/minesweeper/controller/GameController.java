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
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GameController implements GameEventHandler{
    private static GameController myself;
    private GameModel handler;
    private List<DataView> views;
    private LanguageDAO language = LanguageDAO.getInstance();

    private GameController () {
        this.handler = GameModel.getInstance();
        this.handler.setUserActionListener((messageKey, actionKey) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(language.getString("label.askToSave.title"));
            alert.setHeaderText(language.getString(messageKey).replace("_", language.getString(actionKey)));
            alert.setContentText(language.getString("label.askToSave.content"));
            Optional<ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.OK;  // ritorna true se l'utente conferma
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
        handler.newGame();
        views.forEach(DataView::update);
    }

    @Override
    public void save() {
        handler.save();
        views.forEach(DataView::update);
    }

    @Override
    public void saveAs(File file) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(language.getString("label.file.saveAs"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        file = fileChooser.showSaveDialog(null);

        if (file == null) {
            handler.notifyUserFeedback("label.save.err2", UserFeedbackListener.UserFeedbackType.ERROR);
            return;
        }

        // Passa il file al model
        ((GameModel) handler).saveAs(file);

        // Aggiorna le view
        views.forEach(DataView::update);
    }

    @Override
    public void open(File file) {
        if (askToSave("label.open.opengame")) {
            return; // l'utente ha cancellato
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(language.getString("label.open.choosefile"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        file = fileChooser.showOpenDialog(null);

        if (file == null) {
            handler.notifyUserFeedback("label.open.err1", UserFeedbackType.ERROR);
            return;
        }

        // Passa il file al model
        ((GameModel) handler).open(file);

        // Aggiorna le view
        views.forEach(DataView::update);
    }

    @Override
    public void quit() {
        handler.quit();
        views.forEach(DataView::update);
    }

    private boolean askToSave(String actionKey) {
        if (!handler.isGameSavable()) return false;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(language.getString("label.askToSave.title"));
        alert.setHeaderText(language.getString("label.askToSave.header").replace("_", language.getString(actionKey)));
        alert.setContentText(language.getString("label.askToSave.content"));

        // Mostra l'alert e verifica se l'utente ha premuto CANCEL
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.CANCEL;
    }

    private File chooseFileToOpen(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(language.getString("label.open.choosefile"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        return fileChooser.showOpenDialog(stage);
    }

    private File chooseFileToSave(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(language.getString("label.file.saveAs"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        return fileChooser.showSaveDialog(stage);
    }


}
