package deco2800.thomas.events;

import deco2800.thomas.observers.TileEventObservable;
import deco2800.thomas.observers.TileEventObserver;

public class BiomeMusicEvent implements TileEventObserver {

    private String biome;
    private String soundFile;

    /**
     * Constructor - When a player steps onto a biome tile, plays music
     * If biome music is already playing, keep it, else change biome music
     * If biome music changes, fade out other music, wait five seconds then fade in new music
     *
     * TODO: Implement biome type in Tile entity
     *
     * @param biome Biome type
     * @param soundFile Sound file to play
     */
    public void BiomeMusicEvent(String biome, String soundFile) {

    };

    private void playMusic(){};

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