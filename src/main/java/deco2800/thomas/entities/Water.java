package deco2800.thomas.entities;
import deco2800.thomas.worlds.Tile;
import deco2800.thomas.entities.RenderConstants;

public class Water extends StaticEntity {
    private static final String ENTITY_ID_STRING = "water";

    public Water() {
        this.setObjectName(ENTITY_ID_STRING);
    }

    public Water(Tile tile) {
        super(tile, RenderConstants.ROCK_RENDER, "transparent_tile", true);
        this.setObjectName(ENTITY_ID_STRING);
    }

    public Water(Tile tile, boolean obstructed, String texture) {
        super(tile, RenderConstants.ROCK_RENDER, texture, obstructed);
        this.setObjectName(ENTITY_ID_STRING);
    }

    @Override
    public void onTick(long i) {
        //unused
    }
}