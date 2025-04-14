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

    public String getTestPreferences() {
            return model.getPreferences("Test");
    }

    public String getTimePreferences() {
        return model.getPreferences("Time");
    }
}
