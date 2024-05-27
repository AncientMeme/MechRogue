package deco2800.thomas.observers;

import java.util.ArrayList;

public interface TileEventObservable {


    /**
     * Adds tileEventObserver to Observable list
     *
     * @param observer Event that is triggered when player enters tiles
     * */
    void registerObserver(TileEventObserver observer);

    /**
     * Removes tileEventObserver to Observable list
     *
     * @param observer Event that is triggered when player enters tiles
     * */
    void unregisterObserver(TileEventObserver observer);

    /**
     * Notifies observers if the entity moves tiles. To be called onTick
     *
     * */
    void hasChangedTile(boolean hasChangedTile);


    /**
     * Notifies observers of change in tile
     *
     * */
    void notifyObserver();

    /**
     * Helper method to notify observers
     *
     * @param event Event that is triggered when player enters tiles
     * */
    abstract void notifyEvent(TileEventObserver event);
}
