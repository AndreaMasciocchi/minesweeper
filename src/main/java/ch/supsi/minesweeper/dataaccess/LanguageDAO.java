package ch.supsi.minesweeper.dataaccess;

import ch.supsi.minesweeper.controller.UserPreferencesController;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageDAO {
    private ResourceBundle bundle;
    private static LanguageDAO myself;
    private static final UserPreferencesController userPreferencesController=UserPreferencesController.getInstance();
    private static final String[] supportedLanguages = {"en-US", "it-CH"};

    private LanguageDAO(String languageTag) {
        bundle = ResourceBundle.getBundle("i18n.language",Locale.forLanguageTag(checkLanguageTag(languageTag) ? languageTag : supportedLanguages[0]));
    }

    public static LanguageDAO getInstance(){
        if(myself == null)
            myself = new LanguageDAO(userPreferencesController.getLanguagePreferences());
        return myself;
    }

    private static boolean checkLanguageTag(String languageTag) {
        for (int i = 0; i < supportedLanguages.length; i++) {
            if (languageTag.equals(supportedLanguages[i])) {
                return true;
            }
        }
        return false;
    }

    public String getString(String key) {
        return bundle.getString(key);
    }
}
