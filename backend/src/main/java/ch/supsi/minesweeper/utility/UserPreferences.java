package ch.supsi.minesweeper.utility;

import ch.supsi.minesweeper.model.Constant;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import java.io.*;
import java.nio.file.*;

public class UserPreferences {

    private static UserPreferences instance;
    private final Properties preferences = new Properties();

    private UserPreferences() {
        loadPreferences();
    }

    public static synchronized UserPreferences getInstance() {
        if (instance == null) {
            instance = new UserPreferences();
        }
        return instance;
    }

    private void loadPreferences() {
        Path path = Constant.CONFIG_PATH;

        if (Files.exists(path)) {
            try (InputStream in = new FileInputStream(path.toFile())) {
                preferences.load(in);
            } catch (IOException e) {
                System.err.println("Errore nel caricamento preferenze, uso default.");
                setDefaults();
                savePreferences();
            }
        } else {
            setDefaults();
            savePreferences();
        }
    }

    private void setDefaults() {
        preferences.setProperty("bombs", String.valueOf(Constant.DEFAULT_BOMBS));
        preferences.setProperty("language", Constant.DEFAULT_LANGUAGE);
    }

    public void savePreferences() {
        try {
            Files.createDirectories(Constant.CONFIG_PATH.getParent());
            try (OutputStream out = new FileOutputStream(Constant.CONFIG_PATH.toFile())) {
                preferences.store(out, "User Preferences");
            }
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio preferenze.");
        }
    }

    public String getPreference(String key) {
        return preferences.getProperty(key);
    }

    public void setPreference(String key, String value) {
        preferences.setProperty(key, value);
        savePreferences();
    }
}
