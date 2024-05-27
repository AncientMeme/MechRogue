package deco2800.thomas.managers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

//import deco2800.thomas.managers.Manager;

import deco2800.thomas.entities.AgentEntity;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.renderers.PotateCamera;
import deco2800.thomas.worlds.AbstractWorld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
	//debug values stored here
	private int entitiesRendered;
	private int entitiesCount;
	private int tilesRendered;
	private int tilesCount;

	private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);

	private static GameManager instance = null;

	// The list of all instantiated managers classes.
	private List<AbstractManager> managers = new ArrayList<>();

	// The game world currently being played on.
	private AbstractWorld gameWorld;

	// The camera being used by the Game Screen to project the game world.
	private PotateCamera camera;

	// The stage the game world is being rendered on to.
	private Stage stage;

	// The UI skin being used by the game for libGDX elements.
	private Skin skin;

	// Hovered agent entity. Used for non cell based targeting
	// Populated by renderer for efficiency
	private AgentEntity hoveredAgentEntity = null;

	private boolean debugMode = true;

	/**
	 * Whether or not we render info over the tiles.
	 */
	// Whether or not we render the movement path for Players.
	private boolean showCoords = false;
	
	// The game screen for a game that's currently running.
	private boolean showPath = false;

	// Whether to show the collision debug utilities
	private boolean showCollisions = false;

	// Whether to show advanced rendering features
	private boolean showAdvancedRendering = true;

	private float gameLoopTimeMS = 0.0f;

	/**
	 * Whether or not we render info over the entities
	 */
	private boolean showCoordsEntity = false;

	/**
	 * Returns an instance of the GM
	 *
	 * @return GameManager
	 */
	public static GameManager get() {
		if (instance == null) {
			// create a new gameManager
			instance = new GameManager();
		}
		return instance;
	}

	/**
	 * Private constructor to enforce use of get()
	 */
	private GameManager() {

	}

	/**
	 * Add a manager to the current instance, making a new instance if none
	 * exist
	 *
	 * @param manager
	 */
	public static void addManagerToInstance(AbstractManager manager) {
		get().addManager(manager);
	}

	/**
	 * Adds a manager component to the GM
	 *
	 * @param manager
	 */
	public void addManager(AbstractManager manager) {
		managers.add(manager);
	}

	/**
	 * Retrieves a manager from the list.
	 * If the manager does not exist one will be created, added to the list and returned
	 *
	 * @param type The class type (ie SoundManager.class)
	 * @return A Manager component of the requested type
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractManager> T getManager(Class<T> type) {
		/* Check if the manager exists */
		for (AbstractManager m : managers) {
			if (m.getClass() == type) {
				return (T) m;
			}
		}
		LOGGER.info("creating new manager instance");
		/* Otherwise create one */
		AbstractManager newInstance;
		try {
			Constructor<?> ctor = type.getConstructor();
			newInstance = (AbstractManager) ctor.newInstance();
			this.addManager(newInstance);
			return (T) newInstance;
		} catch (Exception e) {
			// Gotta catch 'em all
			LOGGER.error("Exception occurred when adding Manager.");
		}

		LOGGER.warn("GameManager.getManager returned null! It shouldn't have!");
		return null;
	}

	/**
	 * Retrieve a manager from the current GameManager instance, making a new
	 * instance when none are available.
	 *
	 * @param type The class type (ie SoundManager.class)
	 * @return A Manager component of the requested type
	 */
	public static <T extends AbstractManager> T getManagerFromInstance(Class<T> type) {
		return get().getManager(type);
	}

	
	/* ------------------------------------------------------------------------
	 * 				GETTERS AND SETTERS BELOW THIS COMMENT.
	 * ------------------------------------------------------------------------ */

	/**
	 * Get current debug mode
	 */
	public boolean getDebugMode() {
		return this.debugMode;
	}

	/**
	 * Set debug mode to new value
	 * @param mode value to set debugMode to
	 */
	public void setDebugMode(boolean mode) {
		this.debugMode = mode;
	}

	/**
	 * Get current showCollisions value
	 */
	public boolean getCollisions() {
		return this.showCollisions;
	}

	/**
	 * Set showCollisions to new value
	 * @param mode value to set showCollisions to
	 */
	public void setCollisions(boolean mode) {
		this.showCollisions = mode;
	}

	/**
	 * Get current showPath value
	 */
	public boolean getShowPath() {
		return this.showPath;
	}

	/**
	 * Set showPath to new value
	 * @param mode value to set showPath to
	 */
	public void setShowPath(boolean mode) {
		this.showPath = mode;
	}

	/**
	 * Get current showAdvancedRendering value
	 */
	public boolean getShowAdvRender() {
		return this.showAdvancedRendering;
	}

	/**
	 * Set showAdvancedRendering to new value
	 * @param mode value to set showAdvancedRendering to
	 */
	public void setShowAdvRendering(boolean mode) {
		this.showAdvancedRendering = mode;
	}

	/**
	 * Get current showCoords value
	 */
	public boolean getShowCoords() {
		return this.showCoords;
	}

	/**
	 * Set showCoords to new value
	 * @param mode value to set showCoords to
	 */
	public void setShowCoords(boolean mode) {
		this.showCoords = mode;
	}

	/**
	 * Get current showCoordsEntity value
	 */
	public boolean getShowCoordsEntity() {
		return this.showCoordsEntity;
	}

	/**
	 * Set showCoordsEntity to new value
	 * @param mode value to set showCoordsEntity to
	 */
	public void setShowCoordsEntity(boolean mode) {
		this.showCoordsEntity = mode;
	}

	/**Get entities rendered count
	 * @return entities rendered count
	 */
	public int getEntitiesRendered() {
		return this.entitiesRendered;
	}

	/** Set entities rendered to new amount
	 * @param entitiesRendered the new amount
	 */
	public void setEntitiesRendered(int entitiesRendered) {
		this.entitiesRendered = entitiesRendered;
	}
	/**Get number of entities
	 * @return entities count
	 */
	public int getEntitiesCount() {
		return this.entitiesCount;
	}

	/** Set entities count to new amount
	 * @param entitiesCount the new amount
	 */
	public void setEntitiesCount(int entitiesCount) {
		this.entitiesCount = entitiesCount;
	}

	/**Get tiles rendered count
	 * @return tiles rendered count
	 */
	public int getTilesRendered() {
		return this.tilesRendered;
	}

	/** Set tiles rendered to new amount
	 * @param tilesRendered the new amount
	 */
	public void setTilesRendered(int tilesRendered) {
		this.tilesRendered = tilesRendered;
	}

	/**Get number of tiles
	 * @return tiles count
	 */
	public int getTilesCount() {
		return this.tilesCount;
	}

	/** Set tiles count to new amount
	 * @param tilesCount the new amount
	 */
	public void setTilesCount(int tilesCount) {
		this.tilesCount = tilesCount;
	}
	
	/**
	 * Sets the current game world
	 *
	 * @param world
	 */
	public void setWorld(AbstractWorld world) {
		this.gameWorld = world;
	}

	/**
	 * Gets the current game world
	 *
	 * @return the current game world
	 */
	public AbstractWorld getWorld() {
		return gameWorld;
	}


	public void setCamera(PotateCamera camera) {
		this.camera = camera;
	}

	/**
	 * @return current game's stage
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * @param stage - the current game's stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * @return current game's skin
	 */
	public Skin getSkin() {
		return skin;
	}

	/**
	 * @param skin - the current game's skin
	 */
	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	public PotateCamera getCamera() {
		return camera;
	}

	/**
	 * On tick method for ticking managers with the TickableManager interface
	 *
	 * @param i
	 */
	public void onTick(long i) {
		for (AbstractManager m : managers) {
			if (m instanceof TickableManager) {
				((TickableManager) m).onTick(0);
			}
		}
		gameWorld.onTick(0);
	}

	public void setHoveredAgentEntity(AgentEntity hoveredAgentEntity) {
		this.hoveredAgentEntity = hoveredAgentEntity;
	}

	public AgentEntity getHoveredAgentEntity() {
		return hoveredAgentEntity;
	}

	public void setGameLoopTimeMS(float gameLoopTimeMS) {
		this.gameLoopTimeMS = gameLoopTimeMS;
	}

	public float getGameLoopTimeMS() {
		return gameLoopTimeMS;
	}
}
