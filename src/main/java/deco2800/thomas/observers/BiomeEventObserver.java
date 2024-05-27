package deco2800.thomas.observers;

/**
 * An observer interface for entities according to the
 * Observer Design Pattern
 * */
public interface BiomeEventObserver {

    /**
     * Called when the player enters the boundaries of the tile
     *
     * @param biome biome type that the event triggers on.
     * @param player reference to player
     *
     * */
    void notifyEvent(String biome, TileEventObservable player, TileEventObserver event);

}