package deco2800.thomas.tasks;

import deco2800.thomas.entities.AbstractEntity;
import deco2800.thomas.entities.AgentEntity;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.friendlyNPCs.FriendlyPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.PathFindingService;
import deco2800.thomas.util.IntegerPoint;
import deco2800.thomas.util.SquareVector;
import deco2800.thomas.worlds.Tile;

import java.util.List;
import java.util.Random;

/**
 * A task where the entity moves a random distance within a range
 * */
public class WanderTask extends MovementTask{

    /**
     * Task Constructor
     *
     * @param entity The entity thats wandering
     * @param range The range in which the new random destination will fall within
     * */
    public WanderTask (AgentEntity entity, Float range) {
        super(entity, entity.getPosition().randomDistance(range));
    }
}

