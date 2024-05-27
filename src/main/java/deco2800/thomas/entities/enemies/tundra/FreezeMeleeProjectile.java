package deco2800.thomas.entities.enemies.tundra;

import com.badlogic.gdx.graphics.Color;
import deco2800.thomas.entities.DamageIndicator;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.entities.Projectile;
import deco2800.thomas.entities.buffs.FreezeBuff;

public class FreezeMeleeProjectile extends Projectile {
    /* Freeze buff settings on hit */
    private float freezeDuration;
    private float percentDecrease;

    /**
     * Same as Projectile constructor
     */
    public FreezeMeleeProjectile(float row, float col, float direction, float speed, int damage,
                                 float lifetime, Peon enemy, float freezeDuration, float percentDecrease) {
        super(row, col, direction, speed, damage, lifetime, enemy);
        this.freezeDuration = freezeDuration;
        this.percentDecrease = percentDecrease;
        this.setTexture("slash_effect");
    }

    /**
     * Overridden to allow for buff application on hit
     * @param enemy enemy that was hit
     */
    @Override
    public void damageEnemy(Peon enemy) {
        enemy.addHealth(-damage, new DamageIndicator(-damage, Color.SKY));
        enemy.addBuff(new FreezeBuff(freezeDuration, percentDecrease));
    }
}

