package deco2800.thomas.tasks;

import deco2800.thomas.entities.AgentEntity;
import deco2800.thomas.renderers.PauseMenuRenderer;

/**
 * A task that causes the entity to wait the specified number of ticks.
 *
 */
public class SleepTask extends AbstractTask {
    private boolean complete;

    private boolean taskAlive = true;
    private long ticksUntilAwake = 5;
    AgentEntity entity;

    /**
     * Constructor for a SleepTask. Gives the entity a task that does nothing for a number of ticks, as if it were
     * asleep.
     * @param entity Entity to make sleep.
     * @param ticksUntilAwake number of gameticks until the entity wakes up.
     */
    public SleepTask(AgentEntity entity, long ticksUntilAwake) {
        super(entity);

        this.entity = entity;
        this.ticksUntilAwake = ticksUntilAwake;
        this.complete = false;
    }

    /**
     * Ticks the SleepTask task. Just counts down until the entity wakes up.
     * @param tick Current game tick
     */
    @Override
    public void onTick(long tick) {
        if (PauseMenuRenderer.isPaused()) {
            return;
        }
        if (ticksUntilAwake > 0) {
            ticksUntilAwake--;
        } else {
            this.complete = true;
            this.taskAlive = false;
        }
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public boolean isAlive() {
        return taskAlive;
    }

    /**
     * Wakes the entity.
     */
    public void wakeEntity() {
        ticksUntilAwake = 0;
        this.complete = true;
        this.taskAlive = false;
    }

}
