package deco2800.thomas.events;

import deco2800.thomas.observers.TileEventObservable;
import deco2800.thomas.observers.TileEventObserver;

public class BiomeSoundEvent implements TileEventObserver {

    /**
     * When a player steps onto a biome tile, plays sound
     *
     * TODO: Implement biome type in Tile entity
     *
     * @param biome Biome type
     * @param soundFile row index of the tile that the event is associated to
     */
    public void BiomeSoundEvent(String biome, String soundFile) {

    };

    private void playSound(){};

    /**
     * Called when the player enters the boundaries of the tile
     *
     * @param col column index of the tile that the player has moved onto
     * @param row row index of the tile that the player has moved onto
     */
    @Override
    public void notifyEvent(float col, float row, TileEventObservable player, TileEventObserver event) {
        //If the player location is within the biome
    }
}
