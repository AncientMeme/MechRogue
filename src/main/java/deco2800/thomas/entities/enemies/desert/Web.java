package deco2800.thomas.entities.enemies.desert;

import com.badlogic.gdx.graphics.Color;
import deco2800.thomas.entities.DamageIndicator;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.Projectile;
import deco2800.thomas.entities.buffs.BlindBuff;

public class Web extends Projectile {

    Peon enemy;
    float lifetime;
    /**
     * A Web that slows the player on hit.
     *
     * @param row
     * @param col
     * @param direction the direction for the projectile to travel in radians
     * @param speed     the speed of the projectile per second
     * @param damage    the damage of the projectile
     * @param lifetime  the lifetime of the projectile
     * @param enemy     the enemy to check against for collision detection (null means no enemy collision)
     */
    public Web(float row, float col, float direction, float speed, int damage, float lifetime, Peon enemy) {
        super(row, col, direction, speed, damage, lifetime, enemy);
        this.enemy = enemy;
        this.lifetime = lifetime;
        this.setTexture("web");
    }

    /**
     * Ticks the projectile.
     * If a solid entity or target is hit, apply a freeze debuff.
     * If the projectile runs out of lifetime, destroy.
     */
    @Override
    public void onTick(long i) {
        checkEnemyCollision();

        // Assumes 60 FPS
        lifetime -= 1f/60f;
        checkLifetime();
    }

    /**
     * Performs collision against projectile and enemy peon
     */
    private boolean checkEnemyCollision() {
        if (enemy != null) {
            if (enemy.getPosition().isCloseEnoughToBeTheSame(this.getPosition(), 0.75f)) {
                this.enemy.addBuff(new BlindBuff(1.5f));
                return true;
            }
        }
        return false;
    }
}