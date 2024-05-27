package deco2800.thomas.entities;

/**
 * Generic entity used as a response when the collision map is sampled outside
 * the world map. This is not added to the world, just used as an unbreakable
 * StaticEntity
 */
public class WorldBorder extends StaticEntity {
    private static final String ENTITY_ID_STRING = "world_border";

    public WorldBorder() {
        this.setObjectName(ENTITY_ID_STRING);
    }

    @Override
    public void onTick(long i) {
        //unused
    }

    /**
     * Disposing this entity does nothing
     */
    @Override
    public void dispose() {
        //unused
    }
}
