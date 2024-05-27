package deco2800.thomas.managers;

import deco2800.thomas.events.ExampleTileEvent;
import deco2800.thomas.observers.TileEventObservable;

/**
 * TODO: Repurpose this class
 * */
public class PlayerTileEventManager extends AbstractManager{

    private static PlayerTileEventManager instance = null;

    //Add events to player
    public void addTileToPlay (TileEventObservable player) {
        ExampleTileEvent playMusic = new ExampleTileEvent(0, 0);
        player.registerObserver(playMusic);
    }

    /**
     * Returns an instance of the PTEM
     *
     * @return PlayerTileEventManager
     */
    public static PlayerTileEventManager get() {
        if (instance == null) {
            instance = new PlayerTileEventManager();
        }
        return instance;
    }

}
