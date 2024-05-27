package deco2800.thomas.entities;
import deco2800.thomas.worlds.Tile;

public class Rock extends StaticEntity {
    private int health = 100;
    private static final String ENTITY_ID_STRING = "rock";
    
    public Rock() {
        this.setObjectName(ENTITY_ID_STRING);
    }

    public Rock(Tile tile, boolean obstructed) {
        super(tile, RenderConstants.ROCK_RENDER, "rock_1", obstructed);
        this.setObjectName(ENTITY_ID_STRING);
    }

    public Rock(Tile tile, boolean obstructed, String texture) {
        super(tile, RenderConstants.ROCK_RENDER, texture, obstructed);
        this.setObjectName(ENTITY_ID_STRING);
    }

    @Override
    public void onTick(long i) {
        //unused
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void addHealth(int amount) { this.health += amount; }
}
