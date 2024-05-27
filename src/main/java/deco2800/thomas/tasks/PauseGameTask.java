package deco2800.thomas.tasks;

import deco2800.thomas.entities.AgentEntity;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.items.DroppedItem;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.renderers.PauseMenuRenderer;

/**
 * Task responsible for pausing the game
 */
public class PauseGameTask extends AbstractTask {

    private boolean complete;
    private boolean taskAlive = true;


    public PauseGameTask(AgentEntity entity) {
        super(entity);
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isAlive() {
        return taskAlive;
    }

    /**
     * Pauses the game
     * @param tick Current game tick
     */
    public void onTick(long tick) {
        PauseMenuRenderer.togglePause();
        complete = true;
    }
}

