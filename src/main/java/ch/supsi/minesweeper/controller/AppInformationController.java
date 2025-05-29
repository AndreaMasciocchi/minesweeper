package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.AppInformationHandler;
import javafx.scene.control.Alert;

public class AppInformationController implements AppInformationHandler {

    private static AppInformationController myself;

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
}
