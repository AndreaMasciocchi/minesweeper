package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.controller.UserPreferencesInterface;
import ch.supsi.minesweeper.dataaccess.UserPreferencePropertiesDao;

import java.util.Properties;

public class UserPreferencesModel implements UserPreferencesInterface {
    private static UserPreferencesModel model;
    private static final PreferencesDataAccessInterface preferencesDao = UserPreferencePropertiesDao.getInstance();
    private final Properties userPereferences;

    protected UserPreferencesModel() { userPereferences = preferencesDao.getPreferences(); }

    public static UserPreferencesModel getInstance() {
        if (model == null) {
            model = new UserPreferencesModel();
        }

        return model;
    }

    @Override
    public String getPreferences(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }

        if (userPereferences == null) {
            return null;
        }

        return userPereferences.getProperty(key);
    }


}
