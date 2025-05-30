package ch.supsi.minesweeper.model;

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
    @Override
    public String getDefaultPreferences(String key){
        if (key == null || key.isEmpty())
            return null;
        return preferencesDao.getDefaultPreferences().getProperty(key);
    }

    @Override
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
