package deco2800.thomas.entities.enemies.tundra;

import com.badlogic.gdx.graphics.Color;
import deco2800.thomas.entities.DamageIndicator;
import deco2800.thomas.entities.HomingProjectile;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.entities.buffs.BlindBuff;
import deco2800.thomas.entities.buffs.SpeedBuff;

public class BlindnessHomingProjectile extends HomingProjectile {
    /**
     * Same as Projectile constructor
     */
    public BlindnessHomingProjectile(float row, float col, float direction, float speed, int damage, float lifetime, Peon enemy) {
        super(row, col, direction, speed, damage, lifetime, enemy);
        this.setTexture("blindness_projectile");
        attachParticle("resources/particle_files/purpleTrail.party");
    }

    /**
     * Overridden to allow for buff application on hit
     * @param enemy enemy that was hit
     */
    @Override
    public void damageEnemy(Peon enemy) {
        enemy.addHealth(-damage, new DamageIndicator("BLINDED", Color.LIGHT_GRAY));
        enemy.addBuff(new BlindBuff(1.5f));
        enemy.addIndicator(new DamageIndicator(-damage, Color.NAVY));
    }
}
