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
        Path prefsPath = Constant.CONFIG_PATH;

        // Se il file esiste → carico da lì
        if (Files.exists(prefsPath)) {
            try (InputStream in = new FileInputStream(prefsPath.toFile())) {
                preferences.load(in);
                return;
            } catch (IOException e) {
                System.err.println("Errore nel caricamento preferenze utente, uso default.");
            }
        }

        // Se non esiste → carico i default e creo file
        try (InputStream in = getClass().getResourceAsStream(String.valueOf(Constant.CONFIG_PATH))) {
            if (in != null) {
                preferences.load(in);
            }
            //savePreferences(); // salvo subito un file con i default
        } catch (IOException e) {
            System.err.println("Errore nel caricamento preferenze di default.");
        }
    }

    public String getPreference(String key) {
        return preferences.getProperty(key);
    }

    public void setPreference(String key, String value) {
        if (key == null || key.isEmpty()) return;
        preferences.setProperty(key, value);
        savePreferences();
    }

    private void savePreferences() {
        Path prefsPath = Constant.CONFIG_PATH;

        try {
            Files.createDirectories(prefsPath.getParent());
            try (OutputStream out = new FileOutputStream(prefsPath.toFile())) {
                preferences.store(out, "Minesweeper User Preferences");
            }
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio delle preferenze.");
        }
    }
}
