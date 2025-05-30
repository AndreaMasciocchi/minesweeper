package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.AppInformationHandler;
import javafx.scene.control.Alert;

public class AppInformationController implements AppInformationHandler {

    private static AppInformationController myself;

    LanguageDAO language = LanguageDAO.getInstance();

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
}
