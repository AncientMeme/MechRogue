package deco2800.thomas.tasks;

import deco2800.thomas.entities.AgentEntity;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.enemies.EnemyPeon;

public class TestTask extends AbstractTask{
    EnemyPeon entity;

    public TestTask(EnemyPeon entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public void onTick(long tick) {
        entity.getAggressionRange();
    }
}
