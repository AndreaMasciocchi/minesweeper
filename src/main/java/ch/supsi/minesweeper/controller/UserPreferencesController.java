package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.UserPreferencesModel;

public class UserPreferencesController {

    private static UserPreferencesController myself;

    private static final UserPreferencesInterface model = UserPreferencesModel.getInstance();

    protected UserPreferencesController() {}

    public static UserPreferencesController getInstance() {
        if (myself == null) {
            myself = new UserPreferencesController();
        }

        return myself;
    }

    public String getLanguagePreferences() {
            return model.getPreferences("Language");
    }

    public int getMinesPreferences() {
        return Integer.parseInt(model.getPreferences("Mines"));
    }

    public void setLanguagePreferences(String language) {
        model.setPreferences("Language", language);
    }

    public void setMinesPreferences(int mines) {
        model.setPreferences("Mines", String.valueOf(mines));
    }
}
