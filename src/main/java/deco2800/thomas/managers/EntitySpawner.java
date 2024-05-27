package deco2800.thomas.managers;

import deco2800.thomas.entities.enemies.desert.DesertBossMinionPeon;
import deco2800.thomas.entities.enemies.desert.DesertBossPeon;
import deco2800.thomas.entities.enemies.swamp.SwampBossPeon;
import deco2800.thomas.entities.enemies.tundra.TundraBossPeon;
import deco2800.thomas.entities.enemies.tundra.TundraCommonPeon;
import deco2800.thomas.entities.enemies.tundra.TundraMeleePeon;
import deco2800.thomas.entities.enemies.tundra.TundraRangedPeon;
import deco2800.thomas.entities.enemies.volcano.VolcanoBossPeon;
import deco2800.thomas.worlds.AbstractWorld;

import java.util.Random;

/**
 * Entity spawner. Used simply for good code separation
 */
public class EntitySpawner {
    /**
     * Spawns all enemy entities in an abstract world given a terrain manager
     * @param world world to spawn the entities in
     * @param terrainManager terrain manager used to generate the spawning regions
     */
    public static void spawnEntities(AbstractWorld world, TerrainManager terrainManager) {
        Random spawningRandom = new Random();

        /* TUNDRA */
        // Boss
        world.spawnEntityInSpawningRegion(
                spawningRandom,
                new TundraBossPeon(0, 0),
                terrainManager.getEnemySpawningRegion(TerrainManager.Biome.TUNDRA)
        );

        // Melee
        for (int i = 0; i < 3; i++) {
            world.spawnEntityInSpawningRegion(
                    spawningRandom,
                    new TundraMeleePeon(0, 0),
                    terrainManager.getEnemySpawningRegion(TerrainManager.Biome.TUNDRA)
            );
        }

        // Common
        for (int i = 0; i < 2; i++) {
            world.spawnEntityInSpawningRegion(
                    spawningRandom,
                    new TundraCommonPeon(0, 0),
                    terrainManager.getEnemySpawningRegion(TerrainManager.Biome.TUNDRA)
            );
        }

        // Ranged
        for (int i = 0; i < 2; i++) {
            world.spawnEntityInSpawningRegion(
                    spawningRandom,
                    new TundraRangedPeon(0, 0),
                    terrainManager.getEnemySpawningRegion(TerrainManager.Biome.TUNDRA)
            );
        }

        /* DESERT */
        // Boss
        world.spawnEntityInSpawningRegion(
                spawningRandom,
                new DesertBossPeon(0, 0),
                terrainManager.getEnemySpawningRegion(TerrainManager.Biome.DESERT)
        );

        // Mini Boss
        world.spawnEntityInSpawningRegion(
                spawningRandom,
                new DesertBossMinionPeon(0, 0),
                terrainManager.getEnemySpawningRegion(TerrainManager.Biome.DESERT)
        );

        /* SWAMP */
        // Boss
        world.spawnEntityInSpawningRegion(
                spawningRandom,
                new SwampBossPeon(0, 0),
                terrainManager.getEnemySpawningRegion(TerrainManager.Biome.SWAMP)
        );

        /* VOLCANO */
        // Boss
        world.spawnEntityInSpawningRegion(
                spawningRandom,
                new VolcanoBossPeon(0, 0),
                terrainManager.getEnemySpawningRegion(TerrainManager.Biome.VOLCANO)
        );
    }
}
