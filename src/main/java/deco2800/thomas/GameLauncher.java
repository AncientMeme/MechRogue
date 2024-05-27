package deco2800.thomas;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * DesktopLauncher
 * @author Tim Hadwen
 */
public class GameLauncher {

	/**
	 * Private constructor to hide the implicit constructor
	 */
	private GameLauncher () {
	}

	public static GameLauncher get(){
		return new GameLauncher();
	}

	public static void launch(){
		//application config
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new ThomasGame(), config);
		config.width = 1280;
		config.height = 1000;
		config.title = "DECO2800 2020: Polyhedron";
		// set the main program launch entrance object of game with config, launch
		// game
	}


	/**
	 * Main function for the game.
	 * @param arg Command line arguments (we won't use these)
	 */
	@SuppressWarnings("unused")
	public static void main (String[] arg) {
		//application config
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 1000;
		config.title = "DECO2800 2020: Polyhedron";

		config.vSyncEnabled = false;
		config.backgroundFPS = 144;
		config.foregroundFPS = 144;

		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.silent = true;
		settings.pot = true;
		settings.fast = true;
		settings.combineSubdirectories = true;
		settings.paddingX = 1;
		settings.paddingY = 1;
		settings.maxWidth = 4096;
		settings.maxHeight = 4096;
		settings.edgePadding = true;
		settings.useIndexes = false;
		System.out.println("Starting texture packing.");
		TexturePacker.process(settings, "resources/used_images", "resources", "textures");
		System.out.println("Finished texture packing.");
		// set the main program launch entrance object of game with config, launch
		// game
		new LwjglApplication(new ThomasGame(), config);
	}
}