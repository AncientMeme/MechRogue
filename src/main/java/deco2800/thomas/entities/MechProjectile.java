package deco2800.thomas.entities;

import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.renderers.PauseMenuRenderer;
import deco2800.thomas.util.SquareVector;

import java.util.List;

public class MechProjectile extends Projectile {

    PlayerPeon player;
    /**
     * A projectile that targets a clicked position
     * @param direction the direction for the projectile to travel based on clicked position
     * @param speed the speed of the projectile per second
     * @param damage the damage of the projectile
     * @param lifetime the lifetime of the projectile
     */
    public MechProjectile(float row, float col, float direction, float speed, int damage, float lifetime, PlayerPeon player) {
        super(row, col, direction, speed, damage, lifetime, null);
        this.player = player;
    }

    @Override
    public boolean collidesWith(AbstractEntity enemyPeon) {
        return enemyPeon.getPosition().isCloseEnoughToBeTheSame(new SquareVector(this.getCol(), this.getRow()), 1);
    }

    /**
     * Ticks the projectile forward in the direction of the click
     * If a solid entity or target is hit, deplete enemy health and destroy projectile.
     * If the projectile runs out of lifetime, destroy.
     */
    @Override
    public void onTick(long i) {
        if (PauseMenuRenderer.isPaused()) {
            return;
        }
        List<AbstractEntity> entities = GameManager.get().getWorld().getEntities();

        for (AbstractEntity e : entities) {
            if (e instanceof EnemyPeon && this.collidesWith(e)) {
                this.damageEnemy((EnemyPeon) e);
                this.dispose();

                // if the enemy dies add 5 exp
                if (((EnemyPeon) e).getHealth() == 0) {
                    player.addExp(((EnemyPeon) e).getExperience());
                }
            }
        }
        super.onTick(i);
    }
}
