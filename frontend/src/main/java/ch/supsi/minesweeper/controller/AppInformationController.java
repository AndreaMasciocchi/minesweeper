package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.dataaccess.LanguageDAO;
import ch.supsi.minesweeper.dataaccess.UserPreferencePropertiesDAO;
import ch.supsi.minesweeper.model.AppInformationHandler;
import ch.supsi.minesweeper.model.PreferencesDataAccessInterface;
import javafx.scene.control.Alert;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AppInformationController implements AppInformationHandler {

    private static AppInformationController myself;
    private final LanguageDAO language = LanguageDAO.getInstance();
    private final PreferencesDataAccessInterface preferences = UserPreferencePropertiesDAO.getInstance();

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
        try {
            Desktop.getDesktop().open(new File(preferences.getUserPreferencesFilePath().toString()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(language.getString("label.preferences.error"));
            alert.setHeaderText(language.getString("label.preferences.header"));
            alert.setContentText(language.getString("label.preferences.content"));
        }
    }
}
