package ch.supsi.minesweeper.utility;

import ch.supsi.minesweeper.dataaccess.UserPreferencePropertiesDAO;
import ch.supsi.minesweeper.model.PreferencesDataAccessInterface;

import java.util.Properties;

public class UserPreferences {
    private static UserPreferences model;
    private static final PreferencesDataAccessInterface preferencesDao = UserPreferencePropertiesDAO.getInstance();
    private final Properties userPereferences;

    protected UserPreferences() { userPereferences = preferencesDao.getPreferences(); }

    public static UserPreferences getInstance() {
        if (model == null) {
            model = new UserPreferences();
        }

        return model;
    }


    public String getPreferences(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }

        if (userPereferences == null) {
            return null;
        }

        return userPereferences.getProperty(key);
    }

    public String getDefaultPreferences(String key){
        if (key == null || key.isEmpty())
            return null;
        return preferencesDao.getDefaultPreferences().getProperty(key);
    }


    public void setPreferences(String key, String value) {
        if (key == null || key.isEmpty()) {
            return;
        }

        if (userPereferences == null) {
            return;
        }

        userPereferences.setProperty(key, value);
        preferencesDao.setPreferences(userPereferences);
    }


}
