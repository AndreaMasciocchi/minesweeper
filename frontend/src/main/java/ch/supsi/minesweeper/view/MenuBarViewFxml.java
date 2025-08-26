package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.controller.AppInformationController;
import ch.supsi.minesweeper.dataaccess.LanguageDAO;
import ch.supsi.minesweeper.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.io.IOException;
import java.net.URL;

public class MenuBarViewFxml implements ControlledFxView {
    private static MenuBarViewFxml myself;
    private GameEventHandler gameEventHandler;
    private GameInformationHandler gameInformationHandler;
    private final AppInformationHandler appInformationHandler = AppInformationController.getInstance();
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu fileMenu;
    @FXML
    private Menu editMenu;
    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem newMenuItem;
    @FXML
    private MenuItem openMenuItem;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem saveAsMenuItem;
    @FXML
    private MenuItem quitMenuItem;
    @FXML
    private MenuItem preferencesMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private MenuItem helpMenuItem;

    LanguageDAO language = LanguageDAO.getInstance();

    private MenuBarViewFxml() {}

    public static MenuBarViewFxml getInstance() {
        if (myself == null) {
            myself = new MenuBarViewFxml();

            try {
                URL fxmlUrl = MenuBarViewFxml.class.getResource("/menubar.fxml");
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
    public void initialize(EventHandler eventHandler, AbstractModel model) {
        this.createBehaviour();
        this.setLanguage();
        this.gameEventHandler = (GameEventHandler) eventHandler;
        this.gameInformationHandler = (GameModel) model;
    }

    private void createBehaviour() {
        // new
        this.newMenuItem.setOnAction(event -> this.gameEventHandler.newGame());

        // save
        this.saveMenuItem.setOnAction(event -> this.gameEventHandler.save());
        this.saveAsMenuItem.setOnAction(event->this.gameEventHandler.saveAs(null));

        this.openMenuItem.setOnAction(event->this.gameEventHandler.open(null));
        this.quitMenuItem.setOnAction(event->this.gameEventHandler.quit());

        this.newMenuItem.setOnAction(event->this.gameEventHandler.newGame());

        this.preferencesMenuItem.setOnAction(event->this.appInformationHandler.preferences());

        this.aboutMenuItem.setOnAction(actionEvent -> this.appInformationHandler.about());
        this.helpMenuItem.setOnAction(actionEvent -> this.appInformationHandler.help());
    }

    private void setLanguage() {
        //File Menu
        this.fileMenu.setText(language.getString("label.file"));
        this.openMenuItem.setText(language.getString("label.file.open"));
        this.newMenuItem.setText(language.getString("label.file.new"));
        this.saveMenuItem.setText(language.getString("label.file.save"));
        this.saveAsMenuItem.setText(language.getString("label.file.saveAs"));
        this.quitMenuItem.setText(language.getString("label.file.quit"));

        //Edit Menu
        this.editMenu.setText(language.getString("label.edit"));
        this.preferencesMenuItem.setText(language.getString("label.edit.preferences"));

        //Help Menu
        this.helpMenu.setText(language.getString("label.help"));
        this.aboutMenuItem.setText(language.getString("label.about"));
        this.helpMenuItem.setText(language.getString("label.help"));

    }

    @Override
    public Node getNode() {
        return this.menuBar;
    }

    @Override
    public void update() {
        this.saveMenuItem.setDisable(!gameInformationHandler.isGameSavable());
        this.saveAsMenuItem.setDisable(!gameInformationHandler.isGameSavable());
        System.out.println(this.getClass().getSimpleName() + " updated..." + System.currentTimeMillis());
    }

}