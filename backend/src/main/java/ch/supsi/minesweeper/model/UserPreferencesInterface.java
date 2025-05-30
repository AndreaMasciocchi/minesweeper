package ch.supsi.minesweeper.model;

public interface UserPreferencesInterface {
    String getPreferences(String key);
    void setPreferences(String key, String value);
    String getDefaultPreferences(String key);
}
