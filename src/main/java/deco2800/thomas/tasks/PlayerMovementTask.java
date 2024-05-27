package deco2800.thomas.tasks;

import deco2800.thomas.Tickable;
import deco2800.thomas.entities.AgentEntity;
import deco2800.thomas.renderers.PauseMenuRenderer;
import deco2800.thomas.util.SquareVector;

public class PlayerMovementTask extends AbstractTask implements Tickable {
    private boolean complete;
    private boolean taskAlive = true;
    private AgentEntity entity;
    private SquareVector destination;

    public PlayerMovementTask(AgentEntity entity, SquareVector destination) {
        super(entity);

        this.entity = entity;
        this.destination = destination;
        this.complete = false;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isAlive() {
        return taskAlive;
    }

    public void onTick(long tick) {
        if (PauseMenuRenderer.isPaused()) {
            return;
        }
        entity.moveTowards(destination);
        if(entity.getPosition().isCloseEnoughToBeTheSame(destination)) {
            complete = true;
        }
    }
}
