package ch.supsi.minesweeper.model;

import java.nio.file.Path;
import java.util.Properties;

public interface PreferencesDataAccessInterface {
    Properties getPreferences();
    void setPreferences(Properties preferences);
    Properties getDefaultPreferences();
    Path getUserPreferencesFilePath();
}
