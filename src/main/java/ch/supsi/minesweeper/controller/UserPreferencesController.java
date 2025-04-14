package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.UserPreferencesModel;

public class UserPreferencesController {

    private static UserPreferencesController controller;

    private static final UserPreferencesInterface model = UserPreferencesModel.getInstance();

    protected UserPreferencesController() {}

    public static UserPreferencesController getInstance() {
        if (controller == null) {
            controller = new UserPreferencesController();
        }

        return controller;
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
