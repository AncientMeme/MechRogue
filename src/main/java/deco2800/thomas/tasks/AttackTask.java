package deco2800.thomas.tasks;

import deco2800.thomas.entities.AbstractEntity;
import deco2800.thomas.entities.AgentEntity;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.PathFindingService;
import deco2800.thomas.renderers.PauseMenuRenderer;
import deco2800.thomas.util.IntegerPoint;
import deco2800.thomas.util.SquareVector;
import deco2800.thomas.worlds.Tile;

import java.util.List;

public class AttackTask extends MovementTask{

    EnemyPeon entity;
    Peon target;

    private boolean complete = false;

    private boolean computingPath = false;
    private boolean taskAlive = true;

    SquareVector destination;

    private List<IntegerPoint> path;
    private IntegerPoint lastPathfindTarget;
    private MovementTask movementTask;

    private int movementTaskMoveCounter = 0;
    private boolean pathfinding = false;

    /**
     * An attack task that causes the entity to attack the target
     * @param entity entity to do the attacking
     * @param target target entity to attack
     */
    public AttackTask(EnemyPeon entity, Peon target) {
        super(entity, target.getPosition());
        this.entity = entity;
        this.target = target;
        this.destination = target.getPosition();
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
     *  Checks if the entity can attack the target and moves towards it if not.
     * @param tick
     */
    @Override
    public void onTick(long tick) {
        if (PauseMenuRenderer.isPaused()) {
            return;
        }
        entity.setAttackCooldown(entity.getAttackCooldown() - 1);
        if (!entity.isValidTarget(target)) {
            this.taskAlive = false;
            return;
        }

        if (entity.isWithinAggressionRange(target) && !entity.isWithinAttackRange(target)) {
            tryMoveTowardsPlayer();
        }

        //Attack target if it is viable.
        if (entity.isWithinAttackRange(target) && entity.getAttackCooldown() <= 0) {
            entity.attack(target);
            entity.setAttackCooldown(60f/entity.getAttackSpeed());
        }
    }

    /**
     * Encapsulates logic with walking towards the player.
     * If this entity becomes obstructed, fall back to pathfinding
     */
    public void tryMoveTowardsPlayer() {
        if (pathfinding) {
            int moveCounterBeforeTick = movementTaskMoveCounter;
            pathfindToPosition(new IntegerPoint(target.getPosition()));

            if (moveCounterBeforeTick < movementTaskMoveCounter) {
                // Entity pathfinded to a new tile this frame - re think about
                // Whether to exit pathfinding mode or not
                thinkMovementMode();
            }
        } else {
            // If not pathfinding, continually think about what mode to be in
            thinkMovementMode();
            if (!pathfinding) {
                entity.moveTowards(target.getPosition());
            }
        }

    }

    /**
     * Makes the AI think about what movement mode it should currently be in
     */
    public void thinkMovementMode() {
        if (!isObstructed()) {
            pathfinding = false;
        } else {
            pathfinding = true;
        }
    }

    /**
     * Probes collision map based on movement direction
     * @return whether this entity is obstructed
     */
    public boolean isObstructed() {
        boolean obstructed = false;
        int dirX = (int)Math.signum(target.getCol() - entity.getCol());
        int dirY = (int)Math.signum(target.getRow() - entity.getRow());
        if (dirX != 0) {
            SquareVector sample = new SquareVector(
                    (float)Math.floor(entity.getCol()) + dirX + 0.5f,
                    (float)Math.floor(entity.getRow()) + 0.5f
            );
            obstructed |= GameManager.get().getWorld().probeCollisionMap(sample) != null;
        }
        if (dirY != 0) {
            SquareVector sample = new SquareVector(
                    (float)Math.floor(entity.getCol()) + 0.5f,
                    (float)Math.floor(entity.getRow()) + dirY + 0.5f
            );
            obstructed |= GameManager.get().getWorld().probeCollisionMap(sample) != null;
        }
        if (dirX != 0 && dirY != 0) {
            SquareVector sample = new SquareVector(
                    (float)Math.floor(entity.getCol()) + dirX + 0.5f,
                    (float)Math.floor(entity.getRow()) + dirY + 0.5f
            );
            obstructed |= GameManager.get().getWorld().probeCollisionMap(sample) != null;
        }
        return obstructed;
    }

    /**
     *
     * @param pos
     */
    public void pathfindToPosition(IntegerPoint pos) {
        if (!pos.equals(lastPathfindTarget)) {
            movementTask = new MovementTask(this.entity, new SquareVector(pos));
            lastPathfindTarget = pos;
        }

        if (movementTask != null) {
            movementTask.onTick(0);
            movementTaskMoveCounter = movementTask.getMoveCounter();
        }
    }
}
