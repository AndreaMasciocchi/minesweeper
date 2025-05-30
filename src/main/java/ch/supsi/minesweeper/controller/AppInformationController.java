package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.dataaccess.UserPreferencePropertiesDao;
import ch.supsi.minesweeper.model.AppInformationHandler;
import ch.supsi.minesweeper.model.PreferencesDataAccessInterface;
import javafx.scene.control.Alert;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AppInformationController implements AppInformationHandler {

    private static AppInformationController myself;
    private PreferencesDataAccessInterface preferences = UserPreferencePropertiesDao.getInstance();

    public static AppInformationHandler getInstance(){
        if(myself==null){
            myself = new AppInformationController();
        }
        return myself;
    }

    @Override
    public void help() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Come si gioca?");
        alert.setContentText("Ogni turno devi selezionare una casella\n Se fai esplodere una bomba hai perso");
        alert.showAndWait();
    }

    @Override
    public void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Informazioni sull'app");
        alert.setContentText("Minesweeper\nVersione: 1.0.0\nAutori: Mongillo, Masciocchi, Aliprandi");
        alert.showAndWait();
    }

    @Override
    public void preferences() {
        try {
            Desktop.getDesktop().open(new File(preferences.getUserPreferencesFilePath().toString()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error opening the file");
            alert.setHeaderText("An error occurred while reading the preferences file");
        }
    }
}
