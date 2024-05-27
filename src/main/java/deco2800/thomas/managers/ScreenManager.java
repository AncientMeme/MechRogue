package deco2800.thomas.managers;

import deco2800.thomas.GameScreen;
import deco2800.thomas.ThomasGame;

public class  ScreenManager extends AbstractManager {

    /* Represents the current screen displayed in the game */
    private GameScreen currentScreen;

    private static ScreenManager instance = null;

    /**
     * create a instance that controls the screen to be displayed
     * @return
     */
    public static ScreenManager get(){
        if (instance == null) {
            // create a new gameManager
            instance = new ScreenManager();
        }
        return instance;
    }


    /**
     * @return the current screen
     */
    public GameScreen getCurrentScreen() {
        return currentScreen;
    }

    /**
     * Sets the current screen
     * @param screen to set
     */
    public void setCurrentScreen(GameScreen screen) {
        currentScreen = screen;
    }


}
