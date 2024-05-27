package deco2800.thomas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import deco2800.thomas.cutscenes.BeginningCutscene;
import deco2800.thomas.cutscenes.DeathScreen;
import deco2800.thomas.cutscenes.WinCutscene;
import deco2800.thomas.mainmenu.MainMenuScreen;
import deco2800.thomas.managers.GameManager;

/**
 * The game wrapper into which different screens are plugged.
 */
public class ThomasGame extends Game {
	/**
	 * The SpriteBatch for the game.
	 */
	private SpriteBatch batch;
	public static final String SAVE_ROOT_DIR = "thomas-saves";
	private MainMenuScreen mainMenuScreen;
	// beginning cutScene
	private BeginningCutscene beginningCutscene;
	// death screen
	private DeathScreen deathScreen;
	// win scene
	private WinCutscene winCutscene;

	/**
	 * Creates the main menu screen.
	 */
	public void create() {
		FileHandle saveRootHandle = Gdx.files.local(SAVE_ROOT_DIR);
		batch = new SpriteBatch();
		initUISkin();
		beginningCutscene = new BeginningCutscene(this);
		mainMenuScreen = new MainMenuScreen(this);
		deathScreen = new DeathScreen(this);
		winCutscene = new WinCutscene(this);
		// display the beginning cutScene first
		this.setScreen(beginningCutscene);
	}

	/**
	 * Disposes of the game.
	 */
	public void dispose() {
		super.dispose();
		// as the game program has been exit, destroy the scene that has not been
		// destroyed 
		if (beginningCutscene != null){
			beginningCutscene.dispose();
			beginningCutscene = null;
		}
		if (mainMenuScreen != null){
			mainMenuScreen.dispose();
		}
		if(deathScreen != null){
			deathScreen.dispose();
		}
		batch.dispose();
	}

	public void initUISkin() {
		GameManager.get().setSkin(new Skin(Gdx.files.internal("resources/uiskin.skin")));
	}

	/**
	 * show the main start screen and dispose the beginning cutScene
	 */
	public void showStartScreen() {
		// display the main menu screen
		setScreen(mainMenuScreen);

		if (beginningCutscene != null){
			beginningCutscene.dispose();
			// set it to null for avoid using dispose() twice
			beginningCutscene = null;
		}
	}

	public void showDeathScreen(){
		setScreen(deathScreen);
		deathScreen.showAnimation();
	}

	public void showWinScene(){
		setScreen(winCutscene);
	}

	/**
	 * Returns the SpriteBatch for the current instance.
	 */
	public SpriteBatch getBatch() {
		return batch;
	}
}


