package ch.supsi.minesweeper.dataaccess;

import ch.supsi.minesweeper.utility.UserPreferences;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageDAO {
    private ResourceBundle bundle;
    private static LanguageDAO myself;
    private static final String[] supportedLanguages = {"en", "it"};
    private static final UserPreferences model = UserPreferences.getInstance();

    private LanguageDAO(String languageTag) {
        bundle = ResourceBundle.getBundle("language",Locale.forLanguageTag(checkLanguageTag(languageTag) ? languageTag : supportedLanguages[0]));
    }

    public static LanguageDAO getInstance(){
        if(myself == null)
            myself = new LanguageDAO(model.getPreference("language"));
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