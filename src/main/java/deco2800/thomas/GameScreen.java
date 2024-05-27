package deco2800.thomas;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import deco2800.thomas.cutscenes.AnimationContainer;
import deco2800.thomas.cutscenes.DeathFragment;
import deco2800.thomas.cutscenes.OrbFragment;
import deco2800.thomas.entities.AbstractEntity;
import deco2800.thomas.entities.AgentEntity;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.events.ExampleTileEvent;
import deco2800.thomas.handlers.KeyboardManager;

import deco2800.thomas.items.*;
import deco2800.thomas.managers.*;
import deco2800.thomas.observers.KeyDownObserver;
import deco2800.thomas.renderers.*;
import deco2800.thomas.ui.*;
import deco2800.thomas.util.SquareVector;
import deco2800.thomas.util.WorldUtil;
import deco2800.thomas.worlds.*;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import deco2800.thomas.managers.ScreenManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GameScreen implements Screen, KeyDownObserver {

	private final ThomasGame game;
	private static final Logger LOG = LoggerFactory.getLogger(GameScreen.class);

	// display orb cut-scene as beat boss
	private OrbFragment orbFragment;
	// draw textures for contains different image bg

	private Texture orbIceContainer;
	private Texture orbDesertContainer;
	private Texture orbSwampContainer;
	private Texture orbvolcanoContainer;

	private Texture orbIceContainer2;
	private Texture orbDesertContainer2;
	private Texture orbSwampContainer2;
	private Texture orbvolcanoContainer2;

	private Texture orbIceContainer3;
	private Texture orbDesertContainer3;
	private Texture orbSwampContainer3;
	private Texture orbvolcanoContainer3;

	private String tundraBoss = "tundraBossEnemy";
	private String swampBoss = "swampBossEnemy";
	private String volcanoBoss = "volcanoBossEnemy";
	private String desertBoss = "desertBossEnemy";

	private Map<String, Texture> textureMap = new HashMap<>();
	private Map<String, Texture> textureMap2 = new HashMap<>();
	private Map<String, Texture> textureMap3 = new HashMap<>();
	// button for restart game
	private Button restartBtn;
	// button for skip orb cut-scene
	private Button skipBtn;
	private Button nextBtn;

	// texture as restart button up
	private Texture restartButtonUp;
	// texture as restart button down
	private Texture restartButtonDown;

	// texture as skip button up
	private Texture skipButtonUp;
	// texture as skip button down
	private Texture skipButtonDown;


	// texture as next button up
	private Texture nextButtonUp;
	// texture as next button down
	private Texture nextButtonDown;

	// button's press times
	private int pressTime = 0;

	// weather boss died
	private boolean bossDied = false;
	private String bossName = null;

	// orb bar
	private OrbBar orbBar;
	private int numberOfOrbs = 0;
	// orb collector container
	private AnimationContainer animationContainer;

	private ImageButton button;
	private boolean hovered;
	Table table;
	ItemWindow.ItemView itemTable;


	/**
	 * Set the renderer.
	 * 3D is for Isometric worlds
	 * Check the documentation for each renderer to see how it handles WorldEntity coordinates
	 */
	Renderer3D renderer = new Renderer3D();
	OverlayRenderer rendererDebug = new OverlayRenderer();
	InventoryRenderer rendererInventory;
	PauseMenuRenderer pauseMenuRenderer;
	PlayerStatsRenderer rendererStats;
	StatusBarRenderer rendererStatus;
	TextBubble rendererTextBubble;
	AbstractWorld world;
	ItemWindow window;

	// Improve performance by caching these
	private SpriteBatch batchDebug;
	private SpriteBatch batch;

	/**
	 * Create a camera for panning and zooming.
	 * Camera must be updated every render cycle.
	 */
	PotateCamera camera;
	PotateCamera cameraDebug;

	/**
	 * The camera should follow the player when it exists
	 * */
	private PlayerPeon player;


	//display screen
	public static final Stage stage = new Stage(new ExtendViewport(1280, 720));


	long lastGameTick = 0;

	//game types
	public enum gameType {
		LOAD_GAME{
			@Override
			public AbstractWorld method() {
				AbstractWorld world = null;
				try { world = AbstractWorld.deserialize("save.txt");}
				catch(IOException ex)
				{
					LOG.warn("IOException is caught");
				}
				GameManager.get().getManager(NetworkManager.class).startHosting("host");
				return world;
			}
		},
		CONNECT_TO_SERVER{
			@Override
			public AbstractWorld method() {
				AbstractWorld world = new ServerWorld();
				GameManager.get().getManager(NetworkManager.class).connectToHost("localhost", "duck1234");
				return world;
			}
		},
		NEW_GAME{
			@Override
			public AbstractWorld method() {
				AbstractWorld world = new TestWorld();
				GameManager.get().getManager(NetworkManager.class).startHosting("host");
				return world;
			}
		};
		public abstract AbstractWorld method(); // could also be in an interface that MyEnum implements
	  }

	
	public GameScreen(final gameType startType, ThomasGame game) {
		this.game = game;
		/* create and set both death cut-scene and orb cut-scene*/
		// set bg for death cut-scene and orb cut-scenes

		orbIceContainer = new Texture(Gdx.files.internal("resources/orb-pickup_scene/Orb_Pickup_Ice_1.png"));
		orbDesertContainer = new Texture(Gdx.files.internal("resources/orb-pickup_scene/Orb_Pickup_desert_1.png"));
		orbSwampContainer = new Texture(Gdx.files.internal("resources/orb-pickup_scene/Orb_Pickup_swamp_1.png"));
		orbvolcanoContainer = new Texture(Gdx.files.internal("resources/orb-pickup_scene/Orb_Pickup_volcano_1.png"));

		orbIceContainer2 = new Texture(Gdx.files.internal("resources/orb-pickup_scene/Orb_Pickup_Ice_2.png"));
		orbDesertContainer2 = new Texture(Gdx.files.internal("resources/orb-pickup_scene/Orb_Pickup_desert_2.png"));
		orbSwampContainer2 = new Texture(Gdx.files.internal("resources/orb-pickup_scene/Orb_Pickup_swamp_2.png"));
		orbvolcanoContainer2 = new Texture(Gdx.files.internal("resources/orb-pickup_scene/Orb_Pickup_volcano_2.png"));

		orbIceContainer3 = new Texture(Gdx.files.internal("resources/orb-pickup_scene/final_orb_pickup_scenes/Orb_Pickup_ice_3 (1).png"));
		orbDesertContainer3 = new Texture(Gdx.files.internal("resources/orb-pickup_scene/final_orb_pickup_scenes/Orb_Pickup_desert_3 (1).png"));
		orbSwampContainer3 = new Texture(Gdx.files.internal("resources/orb-pickup_scene/final_orb_pickup_scenes/Orb_Pickup_swamp_3 (1).png"));
		orbvolcanoContainer3 = new Texture(Gdx.files.internal("resources/orb-pickup_scene/final_orb_pickup_scenes/Orb_Pickup_volcano_3 (1).png"));


        textureMap.put(tundraBoss, orbIceContainer);
		textureMap.put(swampBoss, orbSwampContainer);
		textureMap.put(volcanoBoss, orbvolcanoContainer);
		textureMap.put(desertBoss, orbDesertContainer);

		textureMap2.put(tundraBoss, orbIceContainer2);
		textureMap2.put(swampBoss, orbSwampContainer2);
		textureMap2.put(volcanoBoss, orbvolcanoContainer2);
		textureMap2.put(desertBoss, orbDesertContainer2);

		textureMap3.put(tundraBoss, orbIceContainer3);
		textureMap3.put(swampBoss, orbSwampContainer3);
		textureMap3.put(volcanoBoss, orbvolcanoContainer3);
		textureMap3.put(desertBoss, orbDesertContainer3);


		// create orb cut-scene
		orbFragment = new OrbFragment(new TextureRegion(orbIceContainer));

		orbFragment.setPosition(stage.getWidth() / 2 - orbFragment.getWidth() / 2,
				stage.getHeight() / 2 - orbFragment.getHeight() / 2 + 20);
		// add orb cut-scene on stage


		/* create and set both skip btn and restart btn*/
		// set input setting into stage
		Gdx.input.setInputProcessor(stage);
		// restart button up and down textures
		restartButtonUp = new Texture(Gdx.files.internal("resources/Cut_scenes/buttonUp.png"));
		restartButtonDown = new Texture(Gdx.files.internal("resources/Cut_scenes/buttonDown.png"));
		// skip button up and down textures
		skipButtonUp = new Texture(Gdx.files.internal("resources/Cut_scenes/skipBtn.png"));
		skipButtonDown = new Texture(Gdx.files.internal("resources/Cut_scenes/skipBtnDown.png"));
		// next button up and down textures
		nextButtonUp = new Texture(Gdx.files.internal("resources/Cut_scenes/nextBtn.png"));
		nextButtonDown = new Texture(Gdx.files.internal("resources/Cut_scenes/nextBtnDown.png"));

		// set the button style
		Button.ButtonStyle style = new Button.ButtonStyle();
		Button.ButtonStyle style1 = new Button.ButtonStyle();
		Button.ButtonStyle style2 = new Button.ButtonStyle();
		// set restart button down and up texture
		style.up = new TextureRegionDrawable(new TextureRegion(restartButtonUp));
		style.down = new TextureRegionDrawable(new TextureRegion(restartButtonDown));
		// set skip button down and up texture
		style1.up = new TextureRegionDrawable(new TextureRegion(skipButtonUp));
		style1.down = new TextureRegionDrawable(new TextureRegion(skipButtonDown));

		// set next button down and up texture
		style2.up = new TextureRegionDrawable(new TextureRegion(nextButtonUp));
		style2.down = new TextureRegionDrawable(new TextureRegion(nextButtonDown));
		// create buttons
		restartBtn = new Button(style);
		skipBtn = new Button(style1);
		nextBtn = new Button(style2);
		// set position of buttons
		restartBtn.setPosition(1000,200);
		skipBtn.setPosition(750,180);
		nextBtn.setPosition(450,180);

		/* set listener for buttons */
		/*
		 * add an listener that player can restart the game as player press it
		 */
		// todo: need restart function -> sprint 4
		restartBtn.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){

				restartBtn.setVisible(false);
				game.showStartScreen();
		}
		});



		nextBtn.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				switchComponent(false,pressTime);
				switchComponent(true,pressTime+1);
				pressTime++;
				if (pressTime == 2){
					skipBtn.setPosition(600,180);
					pressTime = 0;
					nextBtn.setVisible(false);
				}
			}
		});

		/*
		 * skip the orb cut-scene and get a orb as user press it
		 */
		skipBtn.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				switchComponent(false,pressTime);
				orbBar.pickUp(bossName);
				numberOfOrbs++;
				skipBtn.setPosition(750,180);
				animationContainer.action(bossName);
			}
		});


		// create a new game
		GameManager gameManager = GameManager.get();

		ScreenManager screenManager = ScreenManager.get();

		batchDebug = new SpriteBatch();
		batch = new SpriteBatch();

		/* Creates a new World*/
		world = startType.method();

		gameManager.setWorld(world);
		screenManager.setCurrentScreen(this);

		getPlayer();

		ExampleTileEvent playMusic = new ExampleTileEvent(0, 0);
		player.registerObserver(playMusic);

		SoundManager soundManager = SoundManager.get();
		gameManager.addManager(soundManager);

		getPlayer();
		player.registerObserver(soundManager);


		// Add first peon to the world
		camera = new PotateCamera(1920, 1080);
		cameraDebug = new PotateCamera(1920, 1080);

		/* Add item button in the game screen*/
		TextureRegion textureRegion = GameManager.get().getManager(TextureManager.class).getTexture("item_button");
		TextureRegion pressedTextureRegion = GameManager.get().getManager(TextureManager.class).getTexture("item_button_pressed");

		TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);
		TextureRegionDrawable pressedTextureRegionDrawable = new TextureRegionDrawable(pressedTextureRegion);

		button = new ImageButton(textureRegionDrawable);
		button.setPosition(camera.position.x - camera.viewportWidth / 2 + 980,
				camera.position.y + camera.viewportHeight / 2 - 120);
		button.setSize(100,50);
		stage.addActor(button);





		/* Add the window to the stage */

		GameManager.get().setStage(stage);
		GameManager.get().setCamera(camera);


		rendererStats = new PlayerStatsRenderer(player);
		rendererStatus = new StatusBarRenderer(player);
		window = new ItemWindow(player);


		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if (!hovered){
					LOG.info("button was pressed!");
					window.create();
					button.getStyle().imageOver =
							new TextureRegionDrawable(pressedTextureRegionDrawable);
					hovered = true;
				} else {
					window.dispose();
					hovered = false;
				}


			}
		});




		rendererTextBubble = new TextBubble();
		DialogueManager dialogueManager = DialogueManager.get();
		dialogueManager.addTextBubble(rendererTextBubble);

		PathFindingService pathFindingService = new PathFindingService();
		GameManager.get().addManager(pathFindingService);

		
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(GameManager.get().getManager(KeyboardManager.class));
		multiplexer.addProcessor(GameManager.get().getManager(InputManager.class));
		Gdx.input.setInputProcessor(multiplexer);
		
		GameManager.get().getManager(KeyboardManager.class).registerForKeyDown(this);
		GameManager.get().getManager(KeyboardManager.class).registerForKeyDown(this.player);
		GameManager.get().getManager(KeyboardManager.class).registerForKeyUp(this.player);
		GameManager.get().getManager(KeyboardManager.class).registerForTouchDown(this.player);

		rendererStats = new PlayerStatsRenderer(player);
		rendererInventory = new InventoryRenderer(player);
		pauseMenuRenderer = PauseMenuRenderer.get(stage, this, game);

		// create and add orb bar into stage
		orbBar = new OrbBar();

		//create and add animation container into stage
		animationContainer = new AnimationContainer();

	}

	@Override
	public void show() {
		//unused
	}

	/**
	 * Renderer thread
	 * Must update all displayed elements using a Renderer
	 */
	@Override
	public void render(float delta) {

		handleRenderables();

		getPlayer();
		if(player != null && player.getHealth() > 0) {

			// Snap to player if player exists
			cameraSnapToPlayer();
		} else {
			game.showDeathScreen();
		}

		if (this.bossDied){
			stage.addActor(orbFragment);
			stage.addActor(skipBtn);
			stage.addActor(nextBtn);
			switchComponent(true,0);

		}
		// todo: show win cut-scene
		if (this.numberOfOrbs == 4){
			game.showWinScene();
		}


		cameraDebug.position.set(camera.position);
		cameraDebug.update();
		camera.update();

		batchDebug.setProjectionMatrix(cameraDebug.combined);

		batch.setProjectionMatrix(camera.combined);
		
		// Clear the entire display as we are using lazy rendering
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// When lagging takes about 7.7ms
		rerenderMapObjects(batch, camera);

		// Takes about 0.7ms on maximum
		rendererDebug.render(batchDebug, cameraDebug);
		rendererInventory.setPlayer(player);
		rendererInventory.render(batchDebug, cameraDebug);

		table = rendererInventory.getTable();
		table.setPosition(camera.viewportWidth-table.getWidth()*1.4f,
				camera.viewportHeight-table.getHeight());
		itemTable = (ItemWindow.ItemView) window.getTable();





		if (rendererStats.getStatsWindowState()) {
			rendererStats.update();
		}
		if (rendererTextBubble.getTextBubbleState()) {
			rendererTextBubble.update();
		}

		rendererStatus.update();

		/* Refresh the experience UI for if information was updated */
		stage.act(delta);
		stage.draw();
	}

	private void handleRenderables() {
		if (System.currentTimeMillis() - lastGameTick > 32.0f) {
			lastGameTick = System.currentTimeMillis();
			long startNS = System.nanoTime();
			GameManager.get().onTick(0);
			GameManager.get().setGameLoopTimeMS((float)(System.nanoTime() - startNS) / 1000000f);
		}
	}

	/**
	 * Use the selected renderer to render objects onto the map
	 */
	private void rerenderMapObjects(SpriteBatch batch, OrthographicCamera camera) {
		renderer.render(batch, camera);
	}

	/**
	 * Resizes the viewport
	 *
	 * @param width
	 * @param height
	 */
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, false);
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
		
		cameraDebug.viewportWidth = width;
		cameraDebug.viewportHeight = height;
		cameraDebug.update();		
	}

	@Override
	public void pause() {
		//do nothing
	}


	@Override
	public void resume() {
		//do nothing
	}

	@Override
	public void hide() {
		//do nothing
	}
	
	/**
	 * Disposes of assets etc when the rendering system is stopped.
	 */
	@Override
	public void dispose() {
		// Don't need this at the moment
		System.exit(0);
	}

	@Override
	public void notifyKeyDown(int keycode) {
		if (keycode == Input.Keys.F12) {
			GameManager.get().setDebugMode(!GameManager.get().getDebugMode());
		}

		if (keycode == Input.Keys.F7) {
			GameManager.get().setCollisions(!GameManager.get().getCollisions());
		}

		if (keycode == Input.Keys.F6) {
			GameManager.get().setShowAdvRendering(!GameManager.get().getShowAdvRender());
		}

		if (keycode == Input.Keys.F5) {
			world = new TestWorld();
			AbstractEntity.resetID();
			Tile.resetID();
			GameManager gameManager = GameManager.get();
			gameManager.setWorld(world);

			// Add first peon to the world
			world.addEntity(new Peon(0f, 0f, 0.05f, 20));
		}
		
		if (keycode == Input.Keys.F11) { // F11
			GameManager.get().setShowCoords(!GameManager.get().getShowCoords());
			LOG.info("Show coords is now {}", GameManager.get().getShowCoords());
		}
		
		
		if (keycode == Input.Keys.C) { // F11
			GameManager.get().setShowCoords(!GameManager.get().getShowCoords());
			LOG.info("Show coords is now {}", GameManager.get().getShowCoords());
		}

		if (keycode == Input.Keys.F10) { // F10
			GameManager.get().setShowPath(!GameManager.get().getShowPath());
			LOG.info("Show Path is now {}", GameManager.get().getShowPath());
		}

		if (keycode == Input.Keys.F3) { // F3
			// Save the world to the DB
			DatabaseManager.saveWorld(null);
		}

		if (keycode == Input.Keys.F4) { // F4
			// Load the world to the DB
			DatabaseManager.loadWorld(null);
		}

		if (keycode == Input.Keys.K) {
			rendererStats.toggleWindowOpen();
		}

		if (keycode == Input.Keys.COMMA) {
			rendererTextBubble.dispose();
		}

		if (keycode == Input.Keys.NUM_1) {
			DialogueManager dialogueManager = DialogueManager.get();
			dialogueManager.choose(0);
		}

		if (keycode == Input.Keys.NUM_2) {
			DialogueManager dialogueManager = DialogueManager.get();
			dialogueManager.choose(1);
		}
	}

	/**
	 * When a player exists in the game world, snap the camera to the player
	 * */
	public void cameraSnapToPlayer() {
		float[] pos = WorldUtil.colRowToWorldCords(player.getCol(),
				player.getRow());
		camera.position.x = pos[0];
		camera.position.y = pos[1];
	}

	/**
	 * Get the current player in the world
	 * */
	public void getPlayer() {
		if(player == null) {

			List<AgentEntity> entities =
					GameManager.get().getWorld().getSortedAgentEntities();

			for(AbstractEntity entity : entities) {
				if(entity.getObjectName().equals("playerPeon")) {
					player = (PlayerPeon) entity;
					LOG.info("player peon was found");
				}
			}
		}
	}

	/**
	 * handle boss's death to game screen with specific boss name
	 * @param bossName name of boss which died just now
	 */
	public void bossHandleDeath(String bossName, SquareVector bossPosition){
		// specify boss name
		this.bossName = bossName;
		// specify boss position
		// handle boss's death
		this.bossDied = true;

		DroppedItem item = null;
		System.out.println("boss is " + bossName + " " + bossPosition.toString());
		if("tundraBossEnemy".equals(bossName)){
			item = new DroppedItem((int)bossPosition.getRow(), (int)bossPosition.getCol(), 2,
					new ToolKit(1));
		}else if("swampBossEnemy".equals(bossName)){
			item = new DroppedItem((int)bossPosition.getRow(), (int)bossPosition.getCol(), 2,
					new Bow("Wood Bow", "003", 1,
							3, 40, 7, "weapon_003"));
		}else if("volcanoBossEnemy".equals(bossName)){
			item = new DroppedItem((int)bossPosition.getRow(), (int)bossPosition.getCol(), 2,
					new Axe("Great Axe", "004", 5,
							7, 10, 1, "weapon_004"));
		}else if("desertBossEnemy".equals(bossName)){
			item = new DroppedItem((int)bossPosition.getRow(), (int)bossPosition.getCol(), 2,
					new Sword("Long Sword", "001", 5,
							6, 9, 3, "weapon_001"));
		}
		if(item != null) {
			GameManager.get().getWorld().getWorldItems().addItem(
					(int)bossPosition.getRow(), (int)bossPosition.getCol(), item);
			GameManager.get().getWorld().addStaticEntity(item);
		}

	}

	/**
	 * switch the display of orb cut-scene depends on signal
	 * @param signal signal that determines display of orb cut-scene
	 */
	public void switchComponent(Boolean signal, int switchNum){

		if (signal){ // display
			// set the orb cut-scene
			orbFragment.setDrawRegion(new TextureRegion(getTextureMap(switchNum).get(bossName)));
			// display cut-scenes and buttons
			if(!orbFragment.isVisible()){
				orbFragment.setVisible(true);
			}
			if (!nextBtn.isVisible()){
				nextBtn.setVisible(true);
			}
			if (!skipBtn.isVisible()){
				skipBtn.setVisible(true);
			}
		}

		if (!signal) { // hide
			this.bossDied = false;
			// dispose cut-scene
			getTextureMap(switchNum).get(bossName).dispose();
			// hide buttons
			orbFragment.setVisible(false);
			skipBtn.setVisible(false);
			nextBtn.setVisible(false);
		}
	}

	/**
	 * get map specified by press time
	 * @param pressTime how many times button be pressed
	 * @return map contains different part texture of cut-scene
	 */
	public Map<String,Texture> getTextureMap(int pressTime){
		if (pressTime == 0){
			return textureMap;
		}else if (pressTime == 1){
			return textureMap2;
		}else if (pressTime == 2){
			return textureMap3;
		}
		return null;
	}

	public PauseMenuRenderer getPauseMenuRender(){
		return pauseMenuRenderer;
	}

}