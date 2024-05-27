package deco2800.thomas.observers;

import deco2800.thomas.util.SquareVector;

/**
 * An observer interface for entities according to the
 * Observer Design Pattern
 * */
public interface TileEventObserver {

    /**
     * Called when the player enters the boundaries of the tile
     *
     * @require
     *
     * @param col column index of the tile that the player has moved onto
     * @param row row index of the tile that the player has moved onto
     * @param player reference to player
     *
     * */
    void notifyEvent(float col, float row, TileEventObservable player, TileEventObserver event);

}
