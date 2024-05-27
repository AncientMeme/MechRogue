package deco2800.thomas.worlds;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.utils.Array;
import deco2800.thomas.entities.AbstractEntity;
import deco2800.thomas.entities.AgentEntity;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.StaticEntity;
import deco2800.thomas.items.DroppedItem;
import deco2800.thomas.items.WorldItems;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.ParticleManager;
import deco2800.thomas.managers.TerrainManager;
import deco2800.thomas.util.SquareVector;
import deco2800.thomas.util.WorldUtil;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.*;

/**
 * AbstractWorld is the Game AbstractWorld
 *
 * It provides storage for the WorldEntities and other universal world level items.
 */
public abstract class AbstractWorld implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractWorld.class);
    protected List<AbstractEntity> entities = new CopyOnWriteArrayList<>();
    protected int width;
    protected int length;

    protected CopyOnWriteArrayList<Tile> tiles;

    protected List<AbstractEntity> entitiesToDelete = new CopyOnWriteArrayList<>();
    protected List<Tile> tilesToDelete = new CopyOnWriteArrayList<>();
    public static final WorldItems worldItems = new WorldItems();

    protected WorldCollisionMap worldCollisionMap;
    protected int halfWidth;
    protected int halfHeight;

    public TerrainManager terrainManager;

    // Used for collision debug renderer - only gets populated if debug is enabled
    protected List<SquareVector> collisonProbeLog;

    // List of IDs for particles attached to the world
    protected List<Integer> particleIDs;

    protected AbstractWorld(int halfWidth, int halfHeight) {
    	tiles = new CopyOnWriteArrayList<>();
    	// Collision map setup
        worldCollisionMap = new WorldCollisionMap(halfWidth * 2, halfHeight * 2);
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
        this.collisonProbeLog = new ArrayList<>();
        this.collisonProbeLog = new ArrayList<SquareVector>();
        this.particleIDs = new ArrayList<Integer>();

    	generateWorld();
    	generateNeighbours();
    	generateTileIndices();
    }
    
    
    protected abstract void generateWorld();


    /** Generates neighbours for tiles on a world; assigns the sides needed. */
    public void generateNeighbours() {
    // Multiply coords by 2 to remove floats.
    	Map<Integer, Map<Integer, Tile>> tileMap = new HashMap<>();
		Map<Integer, Tile> columnMap;
		for(Tile tile : tiles) {
			columnMap = tileMap.getOrDefault((int)tile.getCol(), new HashMap<>());
			columnMap.put((int) (tile.getRow()), tile);
			tileMap.put((int) (tile.getCol()), columnMap);
		}
		
		for(Tile tile : tiles) {
			int col = (int) (tile.getCol());
			int row = (int) (tile.getRow());
			
			// West
			if(tileMap.containsKey(col - 1)) {
			    tile.addNeighbour(Tile.WEST, tileMap.get(col - 1).get(row));
			}
			
			// Central
			if(tileMap.containsKey(col)) {
				// North
				if (tileMap.get(col).containsKey(row + 1)) {
					tile.addNeighbour(Tile.NORTH, tileMap.get(col).get(row + 1));
				}
				
				// South
				if (tileMap.get(col).containsKey(row - 1)) {
					tile.addNeighbour(Tile.SOUTH,tileMap.get(col).get(row - 1));
				}
			}
			
			// East
			if(tileMap.containsKey(col + 1)) {
                tile.addNeighbour(Tile.EAST,tileMap.get(col + 1).get(row));

			}
		}
    }
    
    private void generateTileIndices() {
    	for(Tile tile : tiles) {
    		tile.calculateIndex();
    	}
    }
    
    /**
     * Returns a list of entities in this world.
     * @return All Entities in the world
     */
    public List<AbstractEntity> getEntities() {
        return new CopyOnWriteArrayList<>(this.entities);
    }
    
    /**
     *  Returns a list of entities in this world, ordered by their render level .
     *  @return all entities in the world 
     */
    public List<AbstractEntity> getSortedEntities(){
		List<AbstractEntity> e = new CopyOnWriteArrayList<>(this.entities);
    	Collections.sort(e);
		return e;
    }


    /**
     *  Returns a list of entities in this world, ordered by their render level.
     *  @return all entities in the world 
     */
    public List<AgentEntity> getSortedAgentEntities(){
        List<AgentEntity> e = this.entities
            .stream()
            .filter(p -> p instanceof AgentEntity)
            .map(p -> (AgentEntity) p)
            .collect(Collectors.toList());

    	Collections.sort(e);
		return e;
    }

    /**
     *  Returns a list of Peons in this world, ordered by their render level.
     *  @return all entities in the world
     */
    public List<Peon> getSortedPeons(){
        List<Peon> e = this.entities
                .stream()
                .filter(p -> p instanceof Peon)
                .map(p -> (Peon) p)
                .collect(Collectors.toList());

        Collections.sort(e);
        return e;
    }

    /**
     * Adds an entity to the world.
     * @param entity the entity to add
     */
    public void addEntity(AbstractEntity entity) {
        entities.add(entity);
    }

    /**
     * Removes an entity from the world.
     * @param entity the entity to remove
     */
    public void removeEntity(AbstractEntity entity) {
        entities.remove(entity);
    }

	public void setEntities(List<AbstractEntity> entities) {
		this.entities = entities;
	}

    public List<Tile> getTileMap() {
        return tiles;
    }

    public Tile getTile(float col, float row) {
    	return getTile(new SquareVector(col,row));
    }
    
    public Tile getTile(SquareVector position) {
        for (Tile t : tiles) {
        	if (t.getCoordinates().equals(position)) {
        		return t;
			}
		}
		return null;
    }

    /**
     * Variant of getTile which supports getting tiles with unconstrained position
     */
    public Tile getFlexibleTile(SquareVector position) {
        SquareVector roundedPosition = new SquareVector((float)Math.floor(position.getCol()),
                (float)Math.floor(position.getRow()));

        for (Tile t : tiles) {
            SquareVector flooredTilePosition = new SquareVector((float)Math.floor(t.getCol()), (float)Math.floor(t.getRow()));
            if (flooredTilePosition.isCloseEnoughToBeTheSame(roundedPosition)) {
                return t;
            }
        }
        return null;
    }
    
    public Tile getTile(int index) {
        for (Tile t : tiles) {
        	if (t.getTileID() == index) {
        		return t;
			}
		}
		return null;
    }

    public void setTileMap(CopyOnWriteArrayList<Tile> tileMap) {
        this.tiles = tileMap;
    }
    

    public void updateTile(Tile tile) {
        for (Tile t : this.tiles) {
            if (t.getTileID() == tile.getTileID()) {
                if (!t.equals(tile)) {
                    this.tiles.remove(t);
                    this.tiles.add(tile);
                }
                return;
            }
        }
        this.tiles.add(tile);
    }

    public void updateEntity(AbstractEntity entity) {
        for (AbstractEntity e : this.entities) {
            if (e.getEntityID() == entity.getEntityID()) {
                this.entities.remove(e);
                this.entities.add(entity);
                return;
            }
        }
        this.entities.add(entity);

        // Since MultiEntities need to be attached to the tiles they live on, setup that connection.
        if (entity instanceof StaticEntity) {
            ((StaticEntity) entity).setup();
        }
    }

    public void onTick(long i) {
        collisonProbeLog.clear();
        for (AbstractEntity e : entitiesToDelete) {
            entities.remove(e);
        }

        for (Tile t : tilesToDelete) {
            tiles.remove(t);
        }
    }

    public void deleteTile(int tileid) {
        Tile tile = GameManager.get().getWorld().getTile(tileid);
        if (tile != null) {
            tile.dispose();
        }
    }

    public void deleteEntity(int entityID) {
        for (AbstractEntity e : this.getEntities()) {
            if (e.getEntityID() == entityID) {
                e.dispose();
            }
        }
    }

    public void deleteEntity(AbstractEntity entity) {
        entitiesToDelete.add(entity);
    }


    public void queueEntitiesForDelete(List<AbstractEntity> entities) {
        entitiesToDelete.addAll(entities);
    }

    public void queueTilesForDelete(List<Tile> tiles) {
        tilesToDelete.addAll(tiles);
    }

    /**
     * Checks and returns entity on a given position.
     * @param position position to check for entities
     * @return Entity on the position. Null if there are no entities on the position
     */
    public AbstractEntity getEntity(SquareVector position) {
        for (AbstractEntity entity : this.getEntities()) {
            if (position.isCloseEnoughToBeTheSame(entity.getPosition(), 0.5f)) {
                return entity;
            }
        }
        return null;
    }

    public void addStaticEntity(StaticEntity se) {
        entities.add(se);
        if(!(se instanceof DroppedItem)){
            addCollisionEntry(
                    (int)Math.floor(se.getCol()),
                    (int)Math.floor(se.getRow()),
                    se
            );
        }
    }

    /**
     * Checks and returns entity on a given position.
     * @param position position to check for entities
     * @return Entity on the position. Null if there are no entities on the position
     */
    public AgentEntity getAgentEntity(SquareVector position) {
        for (AgentEntity entity : this.getSortedAgentEntities()) {
            if (position.isCloseEnoughToBeTheSame(entity.getPosition(), 0.5f)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Registers the entity e in the world's collision map at x, y
     * @param x
     * @param y
     * @param e entity to add to the collision map
     */
    public void addCollisionEntry(int x, int y, AbstractEntity e) {
        worldCollisionMap.addCollisionEntry(x + halfWidth, y + halfHeight, e);
    }

    /**
     * clears the world's collision map at x, y
     * @param x
     * @param y
     */
    public void clearCollisionEntry(int x, int y) {
        worldCollisionMap.clearCollisionEntry(x + halfWidth, y + halfHeight);
    }

    /**
     * Gets the registered collision entity in the world's collision map
     * at x, y (world tile space i.e. entity.getPosition())
     * @param position position to probe in world tile space
     * @return abstract entity if found, else null
     */
    public AbstractEntity probeCollisionMap(SquareVector position) {
        // If in debug mode, add probed position to log for rendering
        if (GameManager.get().getDebugMode()) {
            collisonProbeLog.add(position);
        }
        return getCollisionEntry((int)Math.floor(position.getCol()), (int)Math.floor(position.getRow()));
    }

    /**
     * Gets the registered collision entity in the world's collision map
     * at x, y (world tile space i.e. entity.getPosition()).
     * Variation to be used in a multithreaded environment.
     * @param position position to probe in world tile space
     * @return abstract entity if found, else null
     */
    public AbstractEntity probeCollisionMapMutex(SquareVector position) {
        return getCollisionEntry((int)Math.floor(position.getCol()), (int)Math.floor(position.getRow()));
    }

    /**
     * Gets the registered collision entity in the world's collision map
     * at x, y (collision map space)
     * @param x
     * @param y
     * @return abstract entity if found, else null
     */
    private AbstractEntity getCollisionEntry(int x, int y) {
        return worldCollisionMap.getCollisionEntry(x + halfWidth, y + halfHeight);
    }

    /**
     * Gets world's collision map. Should not be used, except for rendering debug
     */
    public WorldCollisionMap getWorldCollisionMap() {
        return worldCollisionMap;
    }

    /**
     * Gets the collision probe log. Only used for collision debug rendering
     */
    public List<SquareVector> getCollisonProbeLog(){return collisonProbeLog; }

    public WorldItems getWorldItems(){
        return worldItems;
    }

    /**
     * Adds 'entity' to the world in a random location inside of 'spawningRegion'
     * @param random RNG
     * @param entity entity to spawn
     * @param spawningRegion region to spawn the entity within
     */
    public void spawnEntityInSpawningRegion(Random random, AbstractEntity entity, TerrainManager.SpawningRegion spawningRegion) {
        int x = random.nextInt(spawningRegion.x2 - spawningRegion.x1) + spawningRegion.x1;
        int y = random.nextInt(spawningRegion.y2 - spawningRegion.y1) + spawningRegion.y1;

        entity.setCol(x);
        entity.setRow(y);

        addEntity(entity);
    }

    /**
     * Adds a particle to the world at point x, y
     * @param particleFilePath file path for .party file
     */
    public void addParticle(String particleFilePath, float x, float y) {
        // Create particle
        int particleID = GameManager.getManagerFromInstance(ParticleManager.class)
                .addParticleEffect(particleFilePath);

        // Set position
        float worldCoords[] = WorldUtil.colRowToWorldCords(x, y);
        Array<ParticleEmitter> emitters = GameManager.getManagerFromInstance(ParticleManager.class).getParticleEffect(particleID).getEmitters();
        for (ParticleEmitter emitter : emitters) {

            emitter.setPosition(worldCoords[0], worldCoords[1]);
        }

        // Start particle
        GameManager.getManagerFromInstance(ParticleManager.class).getParticleEffect(particleID).start();

        particleIDs.add(particleID);
    }

    /**
     * Returns list of particle IDs attached to the world
     */
    public List<Integer> getParticleIDs() {
        return particleIDs;
    }

    public void serialize(String filename) throws IOException {
        // Serialization

        //Saving of object in a file
        FileOutputStream file = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(file);
        try
        {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                LOG.info("File created: {}", myObj.getName());
            } else {
                LOG.info("File already exists");
            }

            // Method for serialization of object
            out.writeObject(this);
        }
        catch(IOException ex)
        {
            LOG.warn("{0}", ex);
        }
        finally {
            out.close();
            file.close();
        }
    }

    public static AbstractWorld deserialize(String filename) throws IOException {
        AbstractWorld loadedWorld = null;

        // Reading the object from a file
        FileInputStream file = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(file);
        try
        {

            // Method for deserialization of object
            loadedWorld = (AbstractWorld)in.readObject();

        }
        catch(IOException ex)
        {
            LOG.warn("IOException is caught");
        }
        catch(ClassNotFoundException ex)
        {
            LOG.warn("ClassNotFoundException is caught");
        }
        finally {
            in.close();
            file.close();

            LOG.info("Object has been deserialized");
        }
        return loadedWorld;
    }

}
