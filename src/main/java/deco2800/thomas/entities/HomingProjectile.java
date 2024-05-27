package deco2800.thomas.entities;

import deco2800.thomas.renderers.PauseMenuRenderer;

/**
 * Homing projectiles that can bea fired by all types of enemies
 */
public class HomingProjectile extends Projectile {
    /* The target of the homing projectile */
    AbstractEntity target;

    /**
     * A homing projectile that homes in on a specific target
     * @param direction the direction for the projectile to travel in radians
     * @param speed the speed of the projectile per second
     * @param damage the damage of the projectile
     * @param lifetime the lifetime of the projectile
     * @param target the target to home in on
     */
    public HomingProjectile(float row, float col, float direction, float speed, int damage, float lifetime, Peon target) {
        super(row, col, direction, speed, damage, lifetime, target);
        this.target = target;
    }

    /**
     * Ticks the projectile forward in the direction.
     * If a solid entity or target is hit, destroy projectile.
     * If the projectile runs out of lifetime, destroy.
     */
    @Override
    public void onTick(long i) {
        if(PauseMenuRenderer.isPaused()){
            return;
        }
        float dt = 1f/60f;

        if (target != null) {
            setDirection(getDirectionTo(this, target));
        }
        super.onTick(i);
    }
}
