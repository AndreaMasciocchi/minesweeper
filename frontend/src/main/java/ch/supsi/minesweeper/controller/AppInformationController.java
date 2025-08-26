package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.dataaccess.LanguageDAO;
import ch.supsi.minesweeper.model.AppInformationHandler;
import ch.supsi.minesweeper.utility.UserPreferences;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;


public class AppInformationController implements AppInformationHandler {

    private static AppInformationController myself;
    private final LanguageDAO language = LanguageDAO.getInstance();
    private UserPreferences preferences;

    public static AppInformationHandler getInstance(){
        if(myself==null){
            myself = new AppInformationController();
        }
        return myself;
    }

    @Override
    public void help() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(language.getString("label.help"));
        alert.setHeaderText(language.getString("label.help.header"));
        alert.setContentText(language.getString("label.help.content"));
        alert.showAndWait();
    }

    @Override
    public void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(language.getString("label.about"));
        alert.setHeaderText(language.getString("label.about.header"));
        alert.setContentText(language.getString("label.about.content"));
        alert.showAndWait();
    }

    @Override
    public void preferences() {
        UserPreferences userPrefs = UserPreferences.getInstance();

        Stage stage = new Stage();
        stage.setTitle(language.getString("label.preferences.title"));
        stage.initModality(Modality.APPLICATION_MODAL); // blocca la finestra principale

        VBox root = new VBox(15);
        root.setPadding(new Insets(15));

        // --- Campo Lingua ---
        Label labelLanguage = new Label(language.getString("label.preferences.language"));
        ComboBox<String> comboLanguage = new ComboBox<>();
        comboLanguage.getItems().addAll("it", "en"); // aggiungi altre lingue se vuoi
        comboLanguage.setValue(userPrefs.getPreference("language"));
        HBox languageRow = new HBox(10, labelLanguage, comboLanguage);

        // --- Campo Numero Bombe ---
        Label labelBombs = new Label(language.getString("label.preferences.bombs"));
        int currentBombs = 10; // default
        try {
            currentBombs = Integer.parseInt(userPrefs.getPreference("bombs"));
        } catch (NumberFormatException ignored) {}
        Spinner<Integer> spinnerBombs = new Spinner<>(1, 99, currentBombs); // min 1, max 99
        HBox bombsRow = new HBox(10, labelBombs, spinnerBombs);

        root.getChildren().addAll(languageRow, bombsRow);

        // --- Pulsante Salva ---
        Button saveButton = new Button(language.getString("label.preferences.save"));
        saveButton.setOnAction(e -> {
            userPrefs.setPreference("language", comboLanguage.getValue());
            userPrefs.setPreference("bombs", String.valueOf(spinnerBombs.getValue()));
            stage.close();
        });

        root.getChildren().add(saveButton);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }


}
