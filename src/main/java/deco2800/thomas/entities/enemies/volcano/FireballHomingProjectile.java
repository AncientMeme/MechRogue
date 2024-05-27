package deco2800.thomas.entities.enemies.volcano;

import com.badlogic.gdx.graphics.Color;
import deco2800.thomas.entities.DamageIndicator;
import deco2800.thomas.entities.HomingProjectile;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.entities.Projectile;
import deco2800.thomas.entities.buffs.BurnBuff;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.NetworkManager;

public class FireballHomingProjectile extends HomingProjectile {

    Peon enemy;
    /**
     * A homing projectile that homes in on a specific target
     *
     * @param row
     * @param col
     * @param direction the direction for the projectile to travel in radians
     * @param speed     the speed of the projectile per second
     * @param damage    the damage of the projectile
     * @param lifetime  the lifetime of the projectile
     * @param target    the target to home in on
     */
    public FireballHomingProjectile(float row, float col, float direction, float speed, int damage, float lifetime, Peon target) {
        super(row, col, direction, speed, damage, lifetime, target);
        this.enemy = target;
        this.setTexture("fireball");
    }

    /**
     * Overridden to allow for buff application on hit
     * @param enemy enemy that was hit
     */
    @Override
    public void damageEnemy(Peon enemy) {
        enemy.addIndicator(new DamageIndicator("BURNING", Color.ORANGE));
        enemy.addBuff(new BurnBuff(3.0f, 1));
    }

    @Override
    public void dispose() {
        float[] directions = {0, 1, 2, 3, 4, 5, 6, 7};
        for (int i = 0; i < 8; i++) {
            Projectile p = new Projectile(this.getCol(), this.getRow(), (float) (directions[i] * Math.PI * 0.25),
                    2f, 1, 1f, this.enemy);
            p.setTexture("fireball");
            GameManager.get().getWorld().addEntity(p);
        }
        GameManager.get().getManager(NetworkManager.class).deleteEntity(this);
        GameManager.get().getWorld().deleteEntity(this);
    }
}
