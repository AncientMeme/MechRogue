package deco2800.thomas.entities;

import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.renderers.PauseMenuRenderer;
import deco2800.thomas.util.SquareVector;
import deco2800.thomas.worlds.Tile;
import org.lwjgl.util.glu.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for projectiles that can be fired by all types of enemies
 */
public class Projectile extends AbstractEntity {
    /* Direction denotes the projectiles path either (N/E/S/W and NE/NW/SW/SE) */
    private float direction;
    /* The duration the projectile has left in seconds */
    private float lifetime;
    /* Projectile Speed */
    private float speed;
    /* Projectile enemy
    *  Used for efficient damage checks (will be reworked) */
    private Peon enemy;
    /* Projectile Damage */
    protected int damage;

    /**
     * A generic projectile that travels in one direction linearly
     * @param direction the direction for the projectile to travel in radians
     * @param speed the speed of the projectile per second
     * @param damage the damage of the projectile
     * @param lifetime the lifetime of the projectile
     * @param enemy the enemy to check against for collision detection (null means no enemy collision)
     */
    public Projectile(float row, float col, float direction, float speed, int damage, float lifetime, Peon enemy) {
        super(row, col, RenderConstants.PROJECTILE_RENDER);
        this.setTexture("blindness_projectile");
        this.setObjectName("projectile");
        this.setHeight(1);
        this.direction = direction;
        this.lifetime = lifetime;
        this.speed = speed;
        this.enemy = enemy;
        this.damage = damage;
    }

    public void setDirection (float direction) {
        this.direction = direction;
    }

    public float getDirection () {
        return this.direction;
    }

    /**
     * Ticks the projectile forward in the direction.
     * If a solid entity or target is hit, destroy projectile.
     * If the projectile runs out of lifetime, destroy.
     */
    @Override
    public void onTick(long i) {
        if (PauseMenuRenderer.isPaused()) {
            return;
        }
        float dt = 1f/60f;

        float currentRow = this.getRow();
        float currentCol = this.getCol();
        this.setRow(currentRow + (float)Math.cos(direction) * speed * dt);
        this.setCol(currentCol + (float)Math.sin(direction) * speed * dt);

        if (checkStaticCollision()) {
            // If there was a collision, the on tick should be exited,
            // Since the projectile will have been killed
            return;
        }

        if (checkEnemyCollision()) {
            // Same as above
            return;
        }

        // Assumes 60 FPS
        lifetime -= 1f/60f;
        checkLifetime();
    }

    /**
     * Performs collision detection against static objects in the scene
     */
    private boolean checkStaticCollision() {
        // Do collision detection against static objects (CCD is overkill)
        SquareVector predictedPosition = new SquareVector(
                this.getCol() + (float)Math.sin(direction) * 0.25f,
                this.getRow() + (float)Math.cos(direction) * 0.25f
        );
        AbstractEntity entityCollidingWith = GameManager.get().getWorld().probeCollisionMap(predictedPosition);
        if (entityCollidingWith != null) {
            // Maybe do damage to static tile
            //entityCollidingWith.dispose();
            this.dispose();
            return true;
        }
        return false;
    }

    /**
     * Performs collision against projectile and enemy peon
     */
    private boolean checkEnemyCollision() {
        if (enemy != null) {
            if (enemy.getPosition().isCloseEnoughToBeTheSame(this.getPosition(), 0.3f)) {
                this.damageEnemy(enemy);
                this.dispose();
                return true;
            }
        }
        return false;
    }

    /**
     * Method that handles code when the enemy is hit. This is useful
     * to override if a projectile needs to apply a buff on hit
     * @param enemy enemy that was hit
     */
    protected void damageEnemy(Peon enemy) {
        if (enemy instanceof PlayerPeon) {
            enemy.addHealth(-damage * (1 - ((PlayerPeon) enemy).getOverallDefense()/100));
        } else {
            enemy.addHealth(-damage);
        }
    }

    /**
     * Checks if the lifetime has dropped below 0. If so, kill projectile
     */
    protected void checkLifetime() {
        if (lifetime <= 0) {
            // Play destroy anim here
            this.dispose();
        }
    }

    /**
     * Returns the direction from the source to the target entity
     * @param source Source entity.
     * @param target Target entity.
     * @returns float of the direction to the target in radians.
     */
    public static float getDirectionTo(AbstractEntity source, AbstractEntity target) {
        float rowDiff = target.getRow() - source.getRow();
        float colDiff = target.getCol() - source.getCol();
        return (float)Math.atan2(colDiff, rowDiff);
    }
}
