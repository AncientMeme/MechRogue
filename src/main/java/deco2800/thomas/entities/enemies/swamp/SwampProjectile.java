package deco2800.thomas.entities.enemies.swamp;
import com.badlogic.gdx.graphics.Color;
import deco2800.thomas.entities.DamageIndicator;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.entities.Projectile;
import deco2800.thomas.entities.buffs.PoisonBuff;

public class SwampProjectile extends Projectile {
    /**
     * Same as Projectile constructor
     */
    public SwampProjectile(float row, float col, float direction, float speed, int damage, float lifetime, Peon enemy) {
        super(row, col, direction, speed, damage, lifetime, enemy);
        this.setTexture("green_slash");
    }

    /**
     * Overridden to allow for buff application on hit
     * @param enemy enemy that was hit
     */
    @Override
    public void damageEnemy(Peon enemy) {
        enemy.addHealth(-damage, new DamageIndicator(-damage, Color.OLIVE));
        enemy.addBuff(new PoisonBuff(2.0f, 1));
        enemy.addIndicator(new DamageIndicator("POISONED", Color.FOREST));
    }
}
