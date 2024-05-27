package deco2800.thomas.events;

import deco2800.thomas.managers.SoundManager;
import deco2800.thomas.observers.TileEventObservable;
import deco2800.thomas.observers.TileEventObserver;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExampleTileEvent implements TileEventObserver, Serializable {

    /** Event tile col*/
    private int col;
    /** Event tile row*/
    private int row;

    static final Logger LOGGER = Logger.getLogger(ExampleTileEvent.class.getPackage().getName());

    /**
     * Constructor - When a player steps onto a specific tile, plays sound
     *
     * TODO: Implement biome type in Tile entity
     *
     * @param col Tile col
     * @param row Tile row
     */
    public ExampleTileEvent(int col, int row) {
        this.col = col;
        this.row = row;
    }

    private void doSomething(){
        SoundManager soundManager = SoundManager.get();
        soundManager.playMusic("desertEnvMusic.mp3");
    }


    /**
     * Called when the player enters the boundaries of the tile.
     *
     * @param col column index of the tile that the player has moved onto
     * @param row row index of the tile that the player has moved onto
     */
    @Override
    public void notifyEvent(float col, float row, TileEventObservable player, TileEventObserver event) {
        //If the player location is within the boundaries of the tile
        if (col >= this.col && col < this.col + 1 &&
        row >= this.row && row < this.row + 1) {
            doSomething();
            try {
                player.unregisterObserver(event);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Exception has occurred");
            }
        }
    }
}
