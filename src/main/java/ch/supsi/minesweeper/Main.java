package ch.supsi.minesweeper;

import ch.supsi.minesweeper.controller.UserPreferencesController;
import ch.supsi.minesweeper.dataaccess.UserPreferencePropertiesDao;
import ch.supsi.minesweeper.model.PreferencesDataAccessInterface;
import ch.supsi.minesweeper.model.ReadWriteUtilities;

import java.io.File;
import java.util.Properties;

public class Main {

    public static void main( String[] args ) {
        MainFx.main(args);
    }

}
