package deco2800.thomas.worlds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Game;
import deco2800.thomas.entities.*;
import deco2800.thomas.entities.enemies.desert.DesertBossMinionPeon;
import deco2800.thomas.entities.enemies.desert.DesertBossPeon;
import deco2800.thomas.entities.enemies.desert.Web;
import deco2800.thomas.entities.enemies.swamp.SwampBossPeon;
import deco2800.thomas.entities.enemies.tundra.TundraBossPeon;
import deco2800.thomas.entities.enemies.tundra.TundraCommonPeon;
import deco2800.thomas.entities.enemies.tundra.TundraMeleePeon;
import deco2800.thomas.entities.enemies.tundra.TundraRangedPeon;
import deco2800.thomas.entities.enemies.volcano.VolcanoBossPeon;
import deco2800.thomas.entities.friendlyNPCs.*;
import deco2800.thomas.items.DroppedItem;
import deco2800.thomas.items.*;

// Import Weapon types

import deco2800.thomas.managers.*;
import deco2800.thomas.util.SquareVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class TestWorld extends AbstractWorld implements Serializable {

	private ArrayList<AbstractEntity> Bosses = new ArrayList<>();
	private Map<String, Weapon> weaponMap = new HashMap<>();
	private final Logger logger = LoggerFactory.getLogger(TestWorld.class);
	/*
	 * radius for tiles 1 - 7 2 - 19 3 - 37 4 - 61 5 - 91 10 - 331 25 - 1951 50 -
	 * 7,651 100 - 30,301 150 - 67,951 200 - 120601
	 *
	 * N = 1 + 6 * summation[0 -> N]
	 */
	boolean notGenerated = true;
	private final int WORLD_WIDTH = 50; // Height and width vars for the map size; constrains tile gen
	private final int WORLD_HEIGHT = 50; // Note the map will double these numbers (bounds are +/- these limits)

	private static final String wep2 = "weapon_002";
	private static final String wep4 = "weapon_004";

	public TestWorld() {
		super(50, 50);
		initializeWeapons();// Initialize the weapon map for accessing created weapons

	}

	//5 tile building
	private StaticEntity createBuilding1(float col, float row) {
		StaticEntity building;
		List<Part> parts = new ArrayList<Part>();
		String spacman = "spacman_ded";

		parts.add(new Part(new SquareVector(1, -1f), spacman, true));
		parts.add(new Part(new SquareVector(-1, -1f), spacman, true));
		parts.add(new Part(new SquareVector(-1, 1f), spacman, true));
		parts.add(new Part(new SquareVector(1, 1f), spacman, true));
		parts.add(new Part(new SquareVector(0, 0), spacman, true));

		return new StaticEntity(col, row, 1, parts);
	}

	//building with a fence
	private StaticEntity createBuilding2(float col, float row) {

		List<Part> parts = new ArrayList<Part>();
		parts.add(new Part(new SquareVector(0, 0), "buildingA", true));

		// left
		parts.add(new Part(new SquareVector(-2, 0), "fenceN-S", true));
		parts.add(new Part(new SquareVector(-2, 1), "fenceN-S", true));

		// Bottom
		parts.add(new Part(new SquareVector(-1, -1), "fenceE-W", true));
		parts.add(new Part(new SquareVector(0, -1), "fenceE-W", true));
		parts.add(new Part(new SquareVector(1, -1), "fenceE-W", true));

		// Top
		String fenceEW = "fenceE-W";
		parts.add(new Part(new SquareVector(-1, 2), fenceEW, true));
		parts.add(new Part(new SquareVector(0, 2), fenceEW, true));
		parts.add(new Part(new SquareVector(1, 2), fenceEW, true));

		// bottom right corner
		parts.add(new Part(new SquareVector(2, -1), "fenceN-W", true));

		// bottom left
		parts.add(new Part(new SquareVector(-2, -1), "fenceN-E", true));

		// top left
		parts.add(new Part(new SquareVector(-2, 2), "fenceS-E", true));

		// top right
		parts.add(new Part(new SquareVector(2, 2), "fenceS-W", true));

		StaticEntity building = new StaticEntity(col, row, 1, parts);
		entities.add(building);
		return building;

	}

	private void addTree(float col, float row) {
		Map<SquareVector, String> textures = new HashMap<SquareVector, String>();
		Tile t = GameManager.get().getWorld().getTile(col, row);

		if (t != null) {
			if (t.getTexture().equals("water") || t.getTexture().equals("lava") || t.getTexture().equals("ice")) {
				return;
			}
		}

		Random random = new Random();
		int tree_type;
		int min;
		String texture = "tree_";

		/*Assign tree to different quadrants*/
		//If tree is in the north-west make it tundra
		if (col < 0 && row > 0) {
			tree_type = random.nextInt(3);
			texture += tree_type;


			//If the tile is in the north east make it swamp
		} else if (col >= 0 && row > 0) {
			min = 3;
			tree_type = random.nextInt(2) + min;
			texture += tree_type;

			//If the tile is in the south west make it desert
		} else if (col >= 0 && row <= 0) {
			min = 5;
			tree_type = random.nextInt(2) + min;
			texture += tree_type;

			//If the tile is in the south east make it volcanic
		} else if (col < 0 && row <= 0) {
			min = 7;
			tree_type = random.nextInt(2) + min;
			texture += tree_type;

			//else default to tundra tree type
		} else {
			tree_type = random.nextInt(3);
			texture += 1;
		}

		Tree tree = new Tree(t, true, texture);
		addStaticEntity(tree);
	}

	private void addRock(float col, float row) {
		Map<SquareVector, String> textures = new HashMap<SquareVector, String>();
		Tile t = GameManager.get().getWorld().getTile(col, row);
		if (t != null) {
			if (t.getTexture().equals("water") || t.getTexture().equals("lava") || t.getTexture().equals("ice")) {
				return;
			}
		}
		Random random = new Random();
		int rock_type;
		int min;
		String texture = "rock_";

		/*Assign tree to different quadrants*/
		//If tree is in the north-west make it tundra
		if (col < 0 && row > 0) {
			rock_type = random.nextInt(2);
			texture += rock_type;


			//If the tile is in the north east make it swamp
		} else if (col >= 0 && row > 0) {
			min = 3;
			rock_type = random.nextInt(2) + min;
			texture += 2;

			//If the tile is in the south west make it desert
		} else if (col >= 0 && row <= 0) {
			min = 3;
			rock_type = random.nextInt(2) + min;
			texture += rock_type;

			//If the tile is in the south east make it volcanic
		} else if (col < 0 && row <= 0) {
			min = 5;
			rock_type = random.nextInt(2) + min;
			texture += rock_type;

			//else default to tundra rock type
		} else {
			rock_type = random.nextInt(3);
			texture += 1;
		}

		Rock rock = new Rock(t, true, texture);
		addStaticEntity(rock);
	}

	public void addWater(float col, float row) {
		Map<SquareVector, String> textures = new HashMap<SquareVector, String>();
		Tile t = GameManager.get().getWorld().getTile(col, row);

		Water water = new Water(t);
		addStaticEntity(water);
	}

	//this get ran on first game tick so the world tiles exist.
	public void createNature() {
		int tileCount = GameManager.get().getWorld().getTileMap().size();
		System.out.println(tileCount);
		for (int i = 0; i < tileCount; i++) {
			Tile t = GameManager.get().getWorld().getTile(i);
			if (t != null) {
				if (t.isObstructed()) {
					addWater(t.getCol(), t.getRow());
				}
			}
		}
		Random random = new Random();
		// Generate some rocks to mine later
		for (int i = 0; i < 100; i++) {
			Tile t = GameManager.get().getWorld().getTile(random.nextInt(tileCount));
			if (t != null) {
				addRock(t.getCol(), t.getRow());
			}
		}
		// Add some trees
		for (int i = 0; i < 50; i++) {
			Tile t = GameManager.get().getWorld().getTile(random.nextInt(tileCount));
			if (t != null) {
				//entities.add(new Tree(t,true, "tree"));
				addTree(t.getCol(), t.getRow());
			}
		}
		entities.add(createBuilding2(-5, 0f));
	}

	// Create a weapon map
	public void initializeWeapons() {
		weaponMap.put("weapon_001", new Sword("Long Sword", "001", 5,
				6, 9, 3, "weapon_001"));
		weaponMap.put("weapon_002", new Sword("Short Sword", "002", 2,
				4, 6, 5, "weapon_002"));
		weaponMap.put("weapon_003", new Bow("Wood Bow", "003", 1,
				3, 40, 7, "weapon_003"));
		weaponMap.put("weapon_004", new Axe("Great Axe", "004", 5,
				7, 10, 1, "weapon_004"));
		weaponMap.put("weapon_005", new Sword("Dagger", "005", 1,
				3, 2, 9, "weapon_005"));
		weaponMap.put("weapon_006", new Wand("Wand", "006", 1,
				3, 30, 7, "weapon_006"));
	}

	@Override
	protected void generateWorld() {
		terrainManager = new TerrainManager(WORLD_WIDTH, WORLD_HEIGHT);
		tiles = terrainManager.exportTiles();

		EntitySpawner.spawnEntities(this, terrainManager);

		// Create the entities in the game
		addEntity(new PlayerPeon(10f, 5f, 0.13f, 20));

		//Add NPC's
		addEntity(new WizardNPC(8f, 3f, 0.01f, 10));
		addEntity(new RogueNPC(9f, 3f, 0.01f, 10));
		addEntity(new VillagerNPC(10f, 3f, 0.01f, 10));
		addEntity(new MerchantNPC(8f, 0f, 0.01f, 10));
		addEntity(new Wizard2NPC(9f, 0f, 0.01f, 10));
		addEntity(new SnowmanNPC(10f, 0f, 0.01f, 10));
		addEntity(new CamoNPC(11f, 3f, 0.02f, 10));

		// Add fire effect
		StaticEntity fire = new StaticEntity(getTile(0.0f, 3.0f), RenderConstants.ROCK_RENDER, "fire", true);
		addStaticEntity(fire);
	}

	@Override
	public void onTick(long i) {
		super.onTick(i);
		//addTree(0f, 0f);
		for (AbstractEntity e : this.getEntities()) {
			e.onTick(0);
			// Tick animations
			if (e.isAnimated()) {
				e.getAnimation().tickAnimation(1f / 60f);
			}
		}

		if (notGenerated) {
			terrainManager.generateFoliage(this);
			spawnItem(0, 0, new MechSuit(1, "Mech_001"));
			addParticle("resources/particle_files/fire.party", 0.0f, 3.0f);

			int[] randR = getRandomPos();
			spawnItem(0,1,weaponMap.get("weapon_002"));
			randR = getRandomPos();
			spawnItem(randR[0],randR[1],weaponMap.get("weapon_005"));
			randR = getRandomPos();
			spawnItem(randR[0],randR[1],weaponMap.get("weapon_004"));
			randR = getRandomPos();
			spawnItem(randR[0],randR[1],weaponMap.get("weapon_003"));
			randR = getRandomPos();
			spawnItem(randR[0],randR[1],weaponMap.get("weapon_006"));
			randR = getRandomPos();
			spawnItem(randR[0],randR[1],new HealthPotion("Health Potion",
					"001", 1,"potion_001",50));
			randR = getRandomPos();
			spawnItem(randR[0],randR[1],new SpeedPotion("Speed Potion",
					"003", 1,"potion_003",2));
			notGenerated = false;
		}
	}

	/**
	 * @param row row of spawning position
	 * @param col column of spawning position
	 * @param item the item spawned
	 */
	public void spawnItem(int row, int col, UnrenderedItem item) {
		DroppedItem droppedItem = new DroppedItem(row, col, 1, item);
		worldItems.addItem(row, col, droppedItem);
		addStaticEntity(droppedItem);
	}

	public int[] getRandomPos(){
		Random rand = new Random();
		int row, col;
		do {
			row = rand.nextInt(WORLD_HEIGHT * 2);
			col = rand.nextInt(WORLD_WIDTH * 2);
		} while (worldCollisionMap.getCollisionEntry(row - WORLD_HEIGHT, col - WORLD_WIDTH) != null);
		return new int[]{row - WORLD_HEIGHT, col - WORLD_WIDTH};
	}
}

/*
 * print out Neighbours for (Tile tile : tiles) { System.out.println();
 * System.out.println(tile); for (Entry<Integer, Tile> firend :
 * tile.getNeighbours().entrySet()) { switch (firend.getKey()) { case
 * Tile.north: System.out.println("north " +(firend.getValue())); break; case
 * Tile.north_east: System.out.println("north_east " + (firend.getValue()));
 * break; case Tile.north_west: System.out.println("north_west " +
 * (firend.getValue())); break; case Tile.south: System.out.println("south " +
 * (firend.getValue())); break; case Tile.south_east:
 * System.out.println("south_east " +(firend.getValue())); break; case
 * Tile.south_west: System.out.println("south_west " + (firend.getValue()));
 * break; } } }
 * 
 */
