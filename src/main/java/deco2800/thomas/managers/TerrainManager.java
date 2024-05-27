package deco2800.thomas.managers;

import deco2800.thomas.entities.Rock;
import deco2800.thomas.util.PoissonDiscSampler;
import deco2800.thomas.util.SquareVector;
import deco2800.thomas.util.Vector2;
import deco2800.thomas.worlds.AbstractWorld;
import deco2800.thomas.worlds.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The TerrainManage manages generation of the terrain of a world.
 */
public class TerrainManager extends AbstractManager implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(TerrainManager.class);

    RawTile[][] rawTiles;

    private int WORLD_WIDTH = 50; // Height and width vars for the map size; constrains tile gen
    private int WORLD_HEIGHT = 50; // Note the map will double these numbers (bounds are +/- these limits)

    SpawningRegion tundraFightSpawningRegion;
    SpawningRegion swampFightSpawningRegion;
    SpawningRegion desertFightSpawningRegion;
    SpawningRegion volcanoFightSpawningRegion;

    //Tile Bitmasking was achievied using this site as a base:
    //https://gamedevelopment.tutsplus.com/tutorials/how-to-use-tile-bitmasking-to-auto-tile-your-level-layouts--cms-25673

    //TODO: Move Enemy Spawning into here from TestWorld, just pass a list of entities to add in generateWorld.

    //TODO: Move generateNature into here somehow, might be hard since it is run on the first tick of the actual world.
    //TODO: Make it so tree and rock textures change based on Biome.

    //TODO: Write Tests for TerrainManager

    //TODO: Fix bug where large lakes near spawn aren't obstructed ingame.

    /**
     * Constructor for the TerrainManager Class
     * @param world_width Width of the map.
     * @param world_height Height of the map.
     */
    public TerrainManager(int world_width, int world_height) {
        this.WORLD_WIDTH = world_width;
        this.WORLD_HEIGHT = world_height;
        this.rawTiles = new RawTile[world_width * 2][world_height * 2];
        this.randomWorldGen();
    }

    /**
     * Stores useful information about each Biome.
     */
    public enum Biome {
        TUNDRA  (0.3f, 0.3f, 0.5f),
        SWAMP   (0.35f, 0.6f, 0.0f),
        DESERT  (0.01f, 0.2f, 0.3f),
        VOLCANO (0.25f, 0.4f, 0.5f),
        PLAINS  (1.0f, 0.0f, 0.0f);

        private final float water_level;
        private final float foliage_chance;
        private final float rock_chance;
        Biome(float water_level, float foliage_chance, float rock_chance) {
            this.water_level = water_level;
            this.foliage_chance = foliage_chance;
            this.rock_chance = rock_chance;
        }

        /**
         * Gets the Water Level for the Biome
         * @return
         */
        public float getWaterLevel() {
            return water_level;
        }

        /**
         * Gets the Foliage Chance for the Biome
         * @return
         */
        public float getFoliageChance() {
            return foliage_chance;
        }

        /**
         * Gets the Rock Chance for the Biome
         * @return
         */
        public float getRockChance() {
            return rock_chance;
        }

        /**
         * Calculates what terrain that a Tile at the specified elevation should be, based on the elevation.
         * @param elevation Elevation of the Tile.
         * @return What TerrainType the Tile is.
         */
        public TerrainType calculateTerrainType(float elevation) {
            return elevation < this.water_level ? TerrainType.WATER : TerrainType.GROUND;
        }

        @Override
        public String toString() {
            switch (this) {
                case TUNDRA:
                    return "tundra";
                case SWAMP:
                    return "swamp";
                case DESERT:
                    return "desert";
                case VOLCANO:
                    return "volcano";
                case PLAINS:
                    return "plains";
            }
            return "";
        }
    }

    /**
     * Stores the type of terrain that a single Tile is. Room for Improvement.
     */
    private enum TerrainType {
        //TODO: Maybe add some FERTILE or BARREN types in to make tree gen more interesting. Will require adding more
        // level values though.
        WATER,
        GROUND,
        SPAWN
    }

    /**
     * Stores the distance from a Tile to the centre of each biome.
     */
    private static class TileBiomeData implements Serializable{
        // Manhattan distances
        public int tundraDistance;
        public int swampDistance;
        public int desertDistance;
        public int volcanoDistance;
        public int plainsDistance;

        /**
         * Constructor for the TileBiomeData Class
         * @param tundraDistance Distance to the centre of the tundra Biome
         * @param swampDistance Distance to the centre of the swamp Biome
         * @param desertDistance Distance to the centre of the desert Biome
         * @param volcanoDistance Distance to the centre of the volcano Biome
         */
        TileBiomeData(int tundraDistance, int swampDistance, int desertDistance, int volcanoDistance, int plainsDistance) {
            this.tundraDistance = tundraDistance;
            this.swampDistance = swampDistance;
            this.desertDistance = desertDistance;
            this.volcanoDistance = volcanoDistance;
            this.plainsDistance = plainsDistance;
        }

        /**
         * Calculates the prominent Biome of the current TBD. (which Biome centre is the closest)
         * @return the prominent Biome.
         */
        public Biome getProminentBiome() {
            if (tundraDistance <= swampDistance && tundraDistance <= desertDistance && tundraDistance <= volcanoDistance && tundraDistance <= plainsDistance) {
                return Biome.TUNDRA;
            } else if (swampDistance <= desertDistance && swampDistance <= volcanoDistance && swampDistance <= plainsDistance) {
                return Biome.SWAMP;
            } else if (desertDistance <= volcanoDistance && desertDistance <= plainsDistance) {
                return Biome.DESERT;
            } else if (volcanoDistance <= plainsDistance) {
                return Biome.VOLCANO;
            } else {
                return Biome.PLAINS;
            }
        }
    }


    /**
     * A Class that stores all of the raw information needed to create a Tile.
     */
    //TODO: Add storage for the direction it is connecting to etc.       OR
    // Implement that in exportTiles() when the textures are chosen.
    private class RawTile implements Serializable{
        Biome biome;
        TerrainType terrainType;
        TileBiomeData tbd;
        boolean nearBiomeEdge;
        float elevation; //Might not need this one but I keep it just in case.
        int x; //Stores the World's coords, so can be negative.
        int y; //Stores the World's coords, so can be negative.

        Byte bitmask;
        int fancyBitmask;

        //The type of Biome the current tile transitions into.
        Biome transitionBiome;

        /**
         * Constructor for the RawTile Class.
         * @param x The world x position of the Tile
         * @param y The world y position of the Tile
         * @param tbd The TBD of the current Tile
         * @param elevation The elevation of the current Tile.
         */
        RawTile(int x, int y, TileBiomeData tbd, float elevation) {
            this.x = x;
            this.y = y;
            this.tbd = tbd;
            this.biome = tbd.getProminentBiome();
            this.elevation = elevation;
            this.terrainType = this.biome.calculateTerrainType(elevation);
            this.nearBiomeEdge = calculateNearBiomeEdge(1f);
            if (nearBiomeEdge) {
                this.terrainType = TerrainType.GROUND;
            }
        }

        /**
         * Calculates whether the tile is near the edge of a Biome (where 2 Biomes meet).
         * @param whatCountsAsNear The distance from the edge that counts as 'near'
         * @return true if the tile is near to the edge, false otherwise.
         */
        private boolean calculateNearBiomeEdge(float whatCountsAsNear) {
            //This is not elegant, but it works FeelsOkayMan
            boolean tundra_swamp = Math.abs(tbd.tundraDistance - tbd.swampDistance) < whatCountsAsNear;
            boolean tundra_desert = Math.abs(tbd.tundraDistance - tbd.desertDistance) < whatCountsAsNear;
            boolean tundra_volcano = Math.abs(tbd.tundraDistance - tbd.volcanoDistance) < whatCountsAsNear;
            boolean swamp_desert = Math.abs(tbd.swampDistance - tbd.desertDistance) < whatCountsAsNear;
            boolean swamp_volcano = Math.abs(tbd.swampDistance - tbd.volcanoDistance) < whatCountsAsNear;
            boolean desert_volcano = Math.abs(tbd.desertDistance - tbd.volcanoDistance) < whatCountsAsNear;

            // Not at all
            boolean tundra_plains = Math.abs(tbd.tundraDistance - tbd.plainsDistance) < whatCountsAsNear;
            boolean swamp_plains = Math.abs(tbd.swampDistance - tbd.plainsDistance) < whatCountsAsNear;
            boolean desert_plains = Math.abs(tbd.desertDistance - tbd.plainsDistance) < whatCountsAsNear;
            boolean volcano_plains = Math.abs(tbd.volcanoDistance - tbd.plainsDistance) < whatCountsAsNear;

            return tundra_swamp || tundra_desert || tundra_volcano || swamp_desert || swamp_volcano || desert_volcano ||
                    tundra_plains || swamp_plains || desert_plains || volcano_plains;
        }

        public boolean nearBiomeEdge() {
            return this.nearBiomeEdge;
        }

        public Biome getBiome() {
            return biome;
        }

        public RawTile getTopLeftNeighbour() {
            if (this.getTopNeighbour() == null) {
                return null;
            }
            return this.getTopNeighbour().getLeftNeighbour();
        }

        public RawTile getLeftNeighbour() {
            if (this.getX() + WORLD_WIDTH - 1 < 0) {
                return null;
            }
            return rawTiles[this.getX() + WORLD_WIDTH - 1][this.getY() + WORLD_HEIGHT];
        }

        public RawTile getBottomLeftNeighbour() {
            if (this.getBottomNeighbour() == null) {
                return null;
            }
            return this.getBottomNeighbour().getLeftNeighbour();
        }

        public RawTile getTopRightNeighbour() {
            if (this.getTopNeighbour() == null) {
                return null;
            }
            return this.getTopNeighbour().getRightNeighbour();
        }

        public RawTile getRightNeighbour() {
            if (this.getX() + WORLD_WIDTH + 1 >= WORLD_WIDTH * 2) {
                return null;
            }
            return rawTiles[this.getX() + WORLD_WIDTH + 1][this.getY() + WORLD_HEIGHT];
        }

        public RawTile getBottomRightNeighbour() {
            if (this.getBottomNeighbour() == null) {
                return null;
            }
            return this.getBottomNeighbour().getRightNeighbour();
        }

        public RawTile getTopNeighbour() {
            if (this.getY() + WORLD_HEIGHT + 1 >= WORLD_HEIGHT * 2) {
                return null;
            }
            return rawTiles[this.getX() + WORLD_WIDTH][this.getY() + WORLD_HEIGHT + 1];
        }

        public RawTile getBottomNeighbour() {
            if (this.getY() + WORLD_HEIGHT - 1 < 0) {
                return null;
            }
            return rawTiles[this.getX() + WORLD_WIDTH][this.getY() + WORLD_HEIGHT - 1];
        }

        public void setTerrainType(TerrainType terrainType) {
            this.terrainType = terrainType;
        }

        public TerrainType getTerrainType() {
            return terrainType;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setBitmask(Byte bitmask) {
            this.bitmask = bitmask;
        }

        public Byte getBitmask() {
            return bitmask;
        }

        public void setFancyBitmask(int fancyBitmask) {
            this.fancyBitmask = fancyBitmask;
        }

        public int getFancyBitmask() {
            return fancyBitmask;
        }

        public void setTransitionBiome(Biome transitionBiome) {
            this.transitionBiome = transitionBiome;
        }

        public Biome getTransitionBiome() {
            return transitionBiome;
        }
    }

    public static class SpawningRegion implements Serializable{
        public final int x1;
        public final int x2;
        public final int y1;
        public final int y2;

        SpawningRegion(int x1, int x2, int y1, int y2) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        }

        SpawningRegion(SquareVector middle, int halfWidth, int halfHeight) {
            x1 = Math.round(middle.getCol()) - halfWidth;
            x2 = Math.round(middle.getCol()) + halfWidth;
            y1 = Math.round(middle.getRow()) - halfHeight;
            y2 = Math.round(middle.getRow()) + halfHeight;
        }

        public boolean pointInside(int x, int y) {
            return (x >= x1 && x <= x2 && y >= y1 && y <= y2);
        }
    }

    /**
     * Exports the tiles to a CopyOnWriteArrayList for use in an AbstractWorld.
     * @return A CopyOnWriteArrayList<Tile> that contains only Tiles.
     */
    public CopyOnWriteArrayList<Tile> exportTiles() {
        CopyOnWriteArrayList<Tile> tiles = new CopyOnWriteArrayList<Tile>();
        //TODO: Make this a little random and add the new directional textures, probably will
        // involve moving it into seperate functions that each return the proper texture.
        for (int x = -WORLD_WIDTH; x < WORLD_WIDTH; x++) {
            for (int y = -WORLD_HEIGHT; y < WORLD_HEIGHT; y++) {
                RawTile raw_tile = rawTiles[x + WORLD_WIDTH][y + WORLD_HEIGHT];
                Tile new_tile = new Tile("grass_0", x, y);
                switch (raw_tile.getBiome()) {
                    case TUNDRA:
                    case SWAMP:
                    case DESERT:
                    case VOLCANO:
                        new_tile = setTexture(raw_tile, x, y);
                        break;
                    case PLAINS:
                        new_tile = new Tile("plains_grass", x, y);
                }
                tiles.add(new_tile);
            }
        }
        return tiles;
    }

    /**
     * Stores a Tile's data in the TerrainManager as a RawTile.
     * @param x col of the tile in the world
     * @param y row of the tile in thw world
     * @param elevation elevation of the tile
     * @param tbd TBD of the tile
     * @param spawningRegions A List of Spawning Regions for the current world.
     */
    private void storeTile(int x, int y, float elevation, TileBiomeData tbd, List<SpawningRegion> spawningRegions) {
        boolean insideSpawningRegion = false;
        for (SpawningRegion sr : spawningRegions) {
            if (sr.pointInside(x, y)) {
                insideSpawningRegion = true;
                break;
            }
        }
        rawTiles[x + WORLD_WIDTH][y + WORLD_HEIGHT] = new RawTile(x, y, tbd, elevation);
        if (insideSpawningRegion) {
            rawTiles[x + WORLD_WIDTH][y + WORLD_HEIGHT].setTerrainType(TerrainType.SPAWN);
        }
    }

    /**
     * Generates a Random World.
     */
    private void randomWorldGen() {
        int biomeBlending = 5;

        Random random = new Random();

        /* Biome epicenter generation */
        // Pick a point on a unit circle, then step 90 degrees around for other biomes.
        // This enforces evenly spaced biomes
        float tundraAngle = random.nextFloat() * 360.0f;
        float swampAngle = tundraAngle + (360.0f / 4.0f) + (random.nextFloat() * 20.0f - 10.0f);
        float desertAngle = swampAngle + (360.0f / 4.0f) + (random.nextFloat() * 20.0f - 10.0f);
        float volcanoAngle = desertAngle + (360.0f / 4.0f) + (random.nextFloat() * 20.0f - 10.0f);

        SquareVector tundraEpicenter = SquareVector.fromAngleRadius(tundraAngle, WORLD_WIDTH / 2.0f);
        SquareVector swampEpicenter = SquareVector.fromAngleRadius(swampAngle, WORLD_WIDTH / 2.0f);
        SquareVector desertEpicenter = SquareVector.fromAngleRadius(desertAngle, WORLD_WIDTH / 2.0f);
        SquareVector volcanoEpicenter = SquareVector.fromAngleRadius(volcanoAngle, WORLD_WIDTH / 2.0f);
        SquareVector plainsEpicenter = new SquareVector(0.0f, 0.0f);

        /* Spawning region generation */
        tundraFightSpawningRegion = new SpawningRegion(tundraEpicenter,
                random.nextInt(2) + 2, random.nextInt(2) + 2);
        swampFightSpawningRegion = new SpawningRegion(swampEpicenter,
                random.nextInt(2) + 2, random.nextInt(2) + 2);
        desertFightSpawningRegion = new SpawningRegion(desertEpicenter,
                random.nextInt(2) + 2, random.nextInt(2) + 2);
        volcanoFightSpawningRegion = new SpawningRegion(volcanoEpicenter,
                random.nextInt(2) + 2, random.nextInt(2) + 2);

        // List to pass into biome gen to prevent foliage / bodies of water on spawning regions
        List<SpawningRegion> spawningRegions = new ArrayList<>();
        spawningRegions.add(tundraFightSpawningRegion);
        spawningRegions.add(swampFightSpawningRegion);
        spawningRegions.add(desertFightSpawningRegion);
        spawningRegions.add(volcanoFightSpawningRegion);

        // 10x10 spawn
        spawningRegions.add(new SpawningRegion(new SquareVector(0, 0), 5, 5));

        /* Tile generation based on what biome it is classified in */
        for (int x = -WORLD_WIDTH; x < WORLD_WIDTH; x++) {
            for (int y = -WORLD_HEIGHT; y < WORLD_HEIGHT; y++) {
                float elevation = NoiseManager.get().simplexOctaves2DRange(
                        x / 20.0f, y / 15.0f,
                        4, 2.0f, 0.5f, 0f, 1.0f);

                SquareVector tilePosition = new SquareVector(x, y);

                TileBiomeData tbd = new TileBiomeData(
                        Math.round(tundraEpicenter.distance(tilePosition)) + random.nextInt(biomeBlending),
                        Math.round(swampEpicenter.distance(tilePosition)) + random.nextInt(biomeBlending),
                        Math.round(desertEpicenter.distance(tilePosition)) + random.nextInt(biomeBlending),
                        Math.round(volcanoEpicenter.distance(tilePosition)) + random.nextInt(biomeBlending),
                        Math.round(plainsEpicenter.distance(tilePosition)) + random.nextInt(biomeBlending)
                );
                storeTile(x, y, elevation, tbd, spawningRegions);
            }
        }
        clearSmallLakes();
        clearBiomeBorderLakes();
        ensureTwoWideTransitions();
        clearSmallLakes();
        calculateBitmasks();
    }

    public String getRandomFoliage(RawTile rawTile) {
        boolean isRock = rawTile.getBiome().getRockChance() <= new Random().nextFloat();
        switch (rawTile.getBiome()) {
            case TUNDRA:
                if (isRock) {
                    return "rock_0";
                } else {
                    return "tree_0";
                }
            case SWAMP:
                if (isRock) {
                    return "rock_5";
                } else {
                    return "tree_3";
                }
            case DESERT:
                if (isRock) {
                    return "rock_3";
                } else {
                    return "tree_5";
                }
            case VOLCANO:
                if (isRock) {
                    return "rock_5";
                } else {
                    return "tree_7";
                }
            case PLAINS:
                if (isRock) {
                    return "rock_5";
                } else {
                    return "tree_3";
                }
        }
        return "rock_0";
    }

    public void generateFoliage(AbstractWorld world) {
        List<Vector2> foliagePoints = PoissonDiscSampler.GeneratePoints(4.0f, new Vector2(WORLD_WIDTH * 2, WORLD_HEIGHT * 2), 30);
        for (Vector2 foliagePoint : foliagePoints) {
            int x = (int)Math.floor(foliagePoint.getX()) - WORLD_WIDTH;
            int y = (int)Math.floor(foliagePoint.getY()) - WORLD_HEIGHT;
            RawTile rawTile = rawTiles[x + WORLD_WIDTH][y + WORLD_HEIGHT];

            if (rawTile.getTerrainType() == TerrainType.GROUND) {
                float value = NoiseManager.get().simplexOctaves2DRange(
                        x / 50.0f, y / 30.0f,
                        2, 2.0f, 0.5f, 0f, 1.0f);

                if (rawTile.getBiome().getFoliageChance() <= value) {
                    Tile t = GameManager.get().getWorld().getFlexibleTile(new SquareVector(x + 0.5f, y + 0.5f));
                    if (t != null) {
                        Rock rock = new Rock(t, true, getRandomFoliage(rawTile));
                        world.addStaticEntity(rock);
                    }
                }
            }
        }
    }

    /**
     * Clears small (1x1) lakes from the game world by replacing them with ground.
     */
    private void clearSmallLakes() {
        for (int x = 0; x < WORLD_WIDTH * 2; x++) {
            for (int y = 0; y < WORLD_HEIGHT * 2; y++) {
                RawTile t = rawTiles[x][y];
                if (t.getTerrainType() == TerrainType.WATER) {
                    boolean smallLake = true;
                    if (t.getLeftNeighbour() != null && t.getLeftNeighbour().getTerrainType() == TerrainType.WATER) {
                        smallLake = false;
                    }
                    if (t.getRightNeighbour() != null && t.getRightNeighbour().getTerrainType() == TerrainType.WATER) {
                        smallLake = false;
                    }
                    if (t.getTopNeighbour() != null && t.getTopNeighbour().getTerrainType() == TerrainType.WATER) {
                        smallLake = false;
                    }
                    if (t.getBottomNeighbour() != null && t.getBottomNeighbour().getTerrainType() == TerrainType.WATER) {
                        smallLake = false;
                    }
                    if (smallLake) {
                        t.setTerrainType(TerrainType.GROUND);
                    }
                }
            }
        }
    }

    /**
     * Clears Water tile blocks that are touching another Biome's land. This is to minimize the number of transition
     * Tiles needed.
     */
    private void clearBiomeBorderLakes() {
        for (int x = 0; x < WORLD_WIDTH * 2; x++) {
            for (int y = 0; y < WORLD_HEIGHT * 2; y++) {
                RawTile t = rawTiles[x][y];
                if (t.getTerrainType() == TerrainType.WATER) {
                    boolean edgeLake = false;
                    if (t.getTopLeftNeighbour() != null && t.getTopLeftNeighbour().getBiome() != t.getBiome()) {
                        edgeLake = true;
                    }
                    if (t.getLeftNeighbour() != null && t.getLeftNeighbour().getBiome() != t.getBiome()) {
                        edgeLake = true;
                    }
                    if (t.getTopRightNeighbour() != null && t.getTopRightNeighbour().getBiome() != t.getBiome()) {
                        edgeLake = true;
                    }
                    if (t.getRightNeighbour() != null && t.getRightNeighbour().getBiome() != t.getBiome()) {
                        edgeLake = true;
                    }
                    if (t.getBottomLeftNeighbour() != null && t.getBottomLeftNeighbour().getBiome() != t.getBiome()) {
                        edgeLake = true;
                    }
                    if (t.getTopNeighbour() != null && t.getTopNeighbour().getBiome() != t.getBiome()) {
                        edgeLake = true;
                    }
                    if (t.getBottomRightNeighbour() != null && t.getBottomRightNeighbour().getBiome() != t.getBiome()) {
                        edgeLake = true;
                    }
                    if (t.getBottomNeighbour() != null && t.getBottomNeighbour().getBiome() != t.getBiome()) {
                        edgeLake = true;
                    }
                    if (edgeLake) {
                        t.setTerrainType(TerrainType.GROUND);
                    }
                }
            }
        }
    }

    /**
     * Clears Water tiles near biome transitions to ensure there is always 2 wide ground block transitions at the edges.
     * This is to avoid the case where a single block borders both a different biome, and a water block.
     */
    private void ensureTwoWideTransitions() {
        for (int x = 0; x < WORLD_WIDTH * 2; x++) {
            for (int y = 0; y < WORLD_HEIGHT * 2; y++) {
                RawTile t = rawTiles[x][y];
                if (t.getTerrainType() == TerrainType.GROUND) {
                    boolean borderingBiome = false;
                    if (t.getTopLeftNeighbour() != null && t.getTopLeftNeighbour().getBiome() != t.getBiome()) {
                        borderingBiome = true;
                    }
                    if (t.getTopNeighbour() != null && t.getTopNeighbour().getBiome() != t.getBiome()) {
                        borderingBiome = true;
                    }
                    if (t.getTopRightNeighbour() != null && t.getTopRightNeighbour().getBiome() != t.getBiome()) {
                        borderingBiome = true;
                    }
                    if (t.getLeftNeighbour() != null && t.getLeftNeighbour().getBiome() != t.getBiome()) {
                        borderingBiome = true;
                    }
                    if (t.getRightNeighbour() != null && t.getRightNeighbour().getBiome() != t.getBiome()) {
                        borderingBiome = true;
                    }
                    if (t.getBottomLeftNeighbour() != null && t.getBottomLeftNeighbour().getBiome() != t.getBiome()) {
                        borderingBiome = true;
                    }
                    if (t.getBottomNeighbour() != null && t.getBottomNeighbour().getBiome() != t.getBiome()) {
                        borderingBiome = true;
                    }
                    if (t.getBottomRightNeighbour() != null && t.getBottomRightNeighbour().getBiome() != t.getBiome()) {
                        borderingBiome = true;
                    }
                    //Check for Water tiles and replace them if the block si already bordering another biome.
                    if (t.getTopLeftNeighbour() != null && t.getTopLeftNeighbour().getTerrainType() ==
                            TerrainType.WATER && borderingBiome) {
                        t.getTopLeftNeighbour().setTerrainType(TerrainType.GROUND);
                    }
                    if (t.getTopNeighbour() != null && t.getTopNeighbour().getTerrainType() ==
                            TerrainType.WATER && borderingBiome) {
                        t.getTopNeighbour().setTerrainType(TerrainType.GROUND);
                    }
                    if (t.getTopRightNeighbour() != null && t.getTopRightNeighbour().getTerrainType() ==
                            TerrainType.WATER && borderingBiome) {
                        t.getTopRightNeighbour().setTerrainType(TerrainType.GROUND);
                    }
                    if (t.getLeftNeighbour() != null && t.getLeftNeighbour().getTerrainType() ==
                            TerrainType.WATER && borderingBiome) {
                        t.getLeftNeighbour().setTerrainType(TerrainType.GROUND);
                    }
                    if (t.getRightNeighbour() != null && t.getRightNeighbour().getTerrainType() ==
                            TerrainType.WATER && borderingBiome) {
                        t.getRightNeighbour().setTerrainType(TerrainType.GROUND);
                    }
                    if (t.getBottomLeftNeighbour() != null && t.getBottomLeftNeighbour().getTerrainType() ==
                            TerrainType.WATER && borderingBiome) {
                        t.getBottomLeftNeighbour().setTerrainType(TerrainType.GROUND);
                    }
                    if (t.getBottomNeighbour() != null && t.getBottomNeighbour().getTerrainType() ==
                            TerrainType.WATER && borderingBiome) {
                        t.getBottomNeighbour().setTerrainType(TerrainType.GROUND);
                    }
                    if (t.getBottomRightNeighbour() != null && t.getBottomRightNeighbour().getTerrainType() ==
                            TerrainType.WATER && borderingBiome) {
                        t.getBottomRightNeighbour().setTerrainType(TerrainType.GROUND);
                    }
                }
            }
        }
    }

    /**
     * Calculates the bitmasks for each Tile.
     */
    private void calculateBitmasks() {
        for (int x = 0; x < WORLD_WIDTH * 2; x++) {
            for (int y = 0; y < WORLD_HEIGHT * 2; y++) {
                RawTile t = rawTiles[x][y];
                byte bitmask = 0;
                int fancyBitmask = 0;
                Biome transitionBiome = t.getBiome();
                //ORDER IS: TopLeft, Top, TopRight, Left, Right, BottomLeft, Bottom, BottomRight

                bitmask += (t.getTopNeighbour() != null && t.getTopNeighbour().getBiome() == t.getBiome() &&
                                t.getTopNeighbour().getTerrainType() == t.getTerrainType()? 1 : 0);

                bitmask += (t.getLeftNeighbour() != null && t.getLeftNeighbour().getBiome() == t.getBiome() &&
                                t.getLeftNeighbour().getTerrainType() == t.getTerrainType()? 1 : 0) * 2;

                bitmask += (t.getRightNeighbour() != null && t.getRightNeighbour().getBiome() == t.getBiome() &&
                                t.getRightNeighbour().getTerrainType() == t.getTerrainType()? 1 : 0) * 4;

                bitmask += (t.getBottomNeighbour() != null && t.getBottomNeighbour().getBiome() == t.getBiome() &&
                        t.getBottomNeighbour().getTerrainType() == t.getTerrainType()? 1 : 0) * 8;

                fancyBitmask += (t.getTopLeftNeighbour() != null && t.getTopLeftNeighbour().getBiome() == t.getBiome() &&
                        t.getTopLeftNeighbour().getTerrainType() == t.getTerrainType()? 1 : 0);

                fancyBitmask += (t.getTopNeighbour() != null && t.getTopNeighbour().getBiome() == t.getBiome() &&
                        t.getTopNeighbour().getTerrainType() == t.getTerrainType()? 1 : 0) * 2;

                fancyBitmask += (t.getTopRightNeighbour() != null && t.getTopRightNeighbour().getBiome() == t.getBiome() &&
                        t.getTopRightNeighbour().getTerrainType() == t.getTerrainType()? 1 : 0) * 4;

                fancyBitmask += (t.getLeftNeighbour() != null && t.getLeftNeighbour().getBiome() == t.getBiome() &&
                        t.getLeftNeighbour().getTerrainType() == t.getTerrainType()? 1 : 0) * 8;

                fancyBitmask += (t.getRightNeighbour() != null && t.getRightNeighbour().getBiome() == t.getBiome() &&
                        t.getRightNeighbour().getTerrainType() == t.getTerrainType()? 1 : 0) * 16;

                fancyBitmask += (t.getBottomLeftNeighbour() != null && t.getBottomLeftNeighbour().getBiome() == t.getBiome() &&
                        t.getBottomLeftNeighbour().getTerrainType() == t.getTerrainType()? 1 : 0) * 32;

                fancyBitmask += (t.getBottomNeighbour() != null && t.getBottomNeighbour().getBiome() == t.getBiome() &&
                        t.getBottomNeighbour().getTerrainType() == t.getTerrainType()? 1 : 0) * 64;

                fancyBitmask += (t.getBottomRightNeighbour() != null && t.getBottomRightNeighbour().getBiome() == t.getBiome() &&
                        t.getBottomRightNeighbour().getTerrainType() == t.getTerrainType()? 1 : 0) * 128;


                if (t.getTopLeftNeighbour() != null && t.getTopLeftNeighbour().getBiome() != t.getBiome()) {
                    transitionBiome = t.getTopLeftNeighbour().getBiome();
                }
                if (t.getTopNeighbour() != null && t.getTopNeighbour().getBiome() != t.getBiome()) {
                    transitionBiome = t.getTopNeighbour().getBiome();
                }
                if (t.getTopRightNeighbour() != null && t.getTopRightNeighbour().getBiome() != t.getBiome()) {
                    transitionBiome = t.getTopRightNeighbour().getBiome();
                }
                if (t.getLeftNeighbour() != null && t.getLeftNeighbour().getBiome() != t.getBiome()) {
                    transitionBiome = t.getLeftNeighbour().getBiome();
                }
                if (t.getRightNeighbour() != null && t.getRightNeighbour().getBiome() != t.getBiome()) {
                    transitionBiome = t.getRightNeighbour().getBiome();
                }
                if (t.getBottomLeftNeighbour() != null && t.getBottomLeftNeighbour().getBiome() != t.getBiome()) {
                    transitionBiome = t.getBottomLeftNeighbour().getBiome();
                }
                if (t.getBottomNeighbour() != null && t.getBottomNeighbour().getBiome() != t.getBiome()) {
                    transitionBiome = t.getBottomNeighbour().getBiome();
                }
                if (t.getBottomRightNeighbour() != null && t.getBottomRightNeighbour().getBiome() != t.getBiome()) {
                    transitionBiome = t.getBottomRightNeighbour().getBiome();
                }
                t.setBitmask(bitmask);
                t.setFancyBitmask(fancyBitmask);
                t.setTransitionBiome(transitionBiome);
            }
        }
    }

    /**
     * Sets the texture of a volcano tile based on it's bitmask
     * @param raw_tile RawTile data representing the Tile
     * @param x x position of the Tile in the world
     * @param y y position of the Tile in the world
     * @return a Tile object with a set texture and position.
     */
    private Tile setTexture(RawTile raw_tile, int x, int y) {
        Tile new_tile = new Tile(raw_tile.getBiome().toString() + "_ground_255", x, y);
        switch (raw_tile.getBiome()) {
            case TUNDRA:
                new_tile = new Tile(raw_tile.getBiome().toString() + "_ground_255", x, y);
                switch (raw_tile.getTerrainType()) {
                    case WATER:
                        new_tile = new Tile(raw_tile.getBiome().toString() + "_water", x, y);
                        new_tile.setObstructed(true);
                        break;
                    case GROUND:
                        if (raw_tile.getTransitionBiome() == Biome.VOLCANO) {
                            new_tile = new Tile(raw_tile.getBiome().toString() + "_ground_" + 255, x, y);
                        } else {
                            new_tile = new Tile(raw_tile.getBiome().toString() + "_ground_" + raw_tile.getFancyBitmask(), x, y);
                        }
                        break;
                    case SPAWN:
                        new_tile = new Tile(raw_tile.getBiome().toString() + "_spawn_15", x, y);
                        break;
                }
                break;
            case SWAMP:
                new_tile = new Tile(raw_tile.getBiome().toString() + "_ground_255", x, y);
                switch (raw_tile.getTerrainType()) {
                    case WATER:
                        new_tile = new Tile(raw_tile.getBiome().toString() + "_water", x, y);
                        new_tile.setObstructed(true);
                        break;
                    case GROUND:
                        new_tile = new Tile(raw_tile.getBiome().toString() + "_ground_" + raw_tile.getFancyBitmask(), x, y);
                        break;
                    case SPAWN:
                        new_tile = new Tile(raw_tile.getBiome().toString() + "_spawn_15", x, y);
                        break;
                }
                break;
            case DESERT:
                new_tile = new Tile(raw_tile.getBiome().toString() + "_volcano_255", x, y);
                switch (raw_tile.getTerrainType()) {
                    case WATER:
                        new_tile = new Tile(raw_tile.getBiome().toString() + "_water", x, y);
                        new_tile.setObstructed(true);
                        break;
                    case GROUND:
                        if (raw_tile.getTransitionBiome() == Biome.VOLCANO) {
                            new_tile = new Tile(raw_tile.getBiome().toString() + "_volcano_" + raw_tile.getFancyBitmask(), x, y);
                        }
                        break;
                    case SPAWN:
                        new_tile = new Tile(raw_tile.getBiome().toString() + "_volcano_255", x, y);
                        break;
                }
                break;
            case VOLCANO:
                new_tile = new Tile(raw_tile.getBiome().toString() + "_ground_255", x, y);
                switch (raw_tile.getTerrainType()) {
                    case WATER:
                        new_tile = new Tile(raw_tile.getBiome().toString() + "_water", x, y);
                        new_tile.setObstructed(true);
                        break;
                    case GROUND:
                        if (raw_tile.getTransitionBiome() == Biome.TUNDRA) {
                            new_tile = new Tile(raw_tile.getBiome().toString() + "_tundra_" + raw_tile.getFancyBitmask(), x, y);
                        }
                        if (raw_tile.getTransitionBiome() == Biome.VOLCANO) {
                            new_tile = new Tile(raw_tile.getBiome().toString() + "_ground_" + raw_tile.getFancyBitmask(), x, y);
                        }
                        break;
                    case SPAWN:
                        new_tile = new Tile(raw_tile.getBiome().toString() + "_spawn_15", x, y);
                        break;
                }
                break;
            case PLAINS:
                new_tile = new Tile(raw_tile.getBiome().toString() + "_grass", x, y);
                break;
        }
        return new_tile;
    }

    public SpawningRegion getEnemySpawningRegion(Biome biome) {
        switch (biome) {
            case TUNDRA:
                return tundraFightSpawningRegion;
            case SWAMP:
                return swampFightSpawningRegion;
            case DESERT:
                return desertFightSpawningRegion;
            case VOLCANO:
                return volcanoFightSpawningRegion;
        }
        return tundraFightSpawningRegion;
    }

    public Biome getBiomeAtTile(int x, int y) {
        return rawTiles[x + WORLD_WIDTH][y + WORLD_HEIGHT].getBiome();
    }
}
