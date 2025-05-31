package ch.supsi.minesweeper.dataaccess;

import ch.supsi.minesweeper.model.UserPreferencesInterface;
import ch.supsi.minesweeper.model.UserPreferencesModel;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageDAO {
    private ResourceBundle bundle;
    private static LanguageDAO myself;
    private static final String[] supportedLanguages = {"en-US", "it-CH"};
    private static final UserPreferencesInterface model = UserPreferencesModel.getInstance();

    private LanguageDAO(String languageTag) {
        //bundle = ResourceBundle.getBundle("i18n.language",Locale.forLanguageTag(checkLanguageTag(languageTag) ? languageTag : supportedLanguages[0]));
        //bundle = ResourceBundle.getBundle("language",Locale.US);
        bundle = ResourceBundle.getBundle("language",Locale.forLanguageTag(checkLanguageTag(languageTag) ? languageTag : supportedLanguages[0]));
    }

    public static LanguageDAO getInstance(){
        if(myself == null)
            myself = new LanguageDAO(model.getPreferences("Language"));
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


/**
 *     public String getLanguagePreferences() {
 *             return model.getPreferences("Language");
 *     }
 *
 *     public int getMinesPreferences() {
 *         return Integer.parseInt(model.getPreferences("Mines"));
 *     }
 *
 *     public void setLanguagePreferences(String language) {
 *         model.setPreferences("Language", language);
 *     }
 *
 *     public void setMinesPreferences(int mines) {
 *         model.setPreferences("Mines", String.valueOf(mines));
 *     }
 */