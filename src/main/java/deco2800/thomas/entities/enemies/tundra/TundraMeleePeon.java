package deco2800.thomas.entities.enemies.tundra;

import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.Projectile;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.SpriteSheetAnimation;

public class TundraMeleePeon extends EnemyPeon {
    /**
     * Constructor for a TundraMeleePeon.
     * @param row Row to spawn the TundraMeleePeon on.
     * @param col Column to spawn the TundraMeleePeon on.
     */
    public TundraMeleePeon (float row, float col) {
        // Use 0.05 as the speed for this enemy
        super(row, col, 0.06f, 20, 5, 1.5f, 1f, 20);
        this.setObjectName("tundraMeleeEnemy");
        SpriteSheetAnimation animation = new SpriteSheetAnimation("tundra_melee_spritesheet",
                0.1f);
        this.setAnimation(animation);
    }

    /**
     * Launches an attack towards the specified Entity.
     * @param target Entity to launch an attack towards.
     */
    @Override
    public void attack(Peon target) {
        Projectile p = new FreezeMeleeProjectile(this.getCol(), this.getRow(), Projectile.getDirectionTo(this, target),
                20f, 1, this.getAttackRange() / 20f, target, 5, 0.1f);
        p.setTexture("slash_effect");
        GameManager.get().getWorld().addEntity(p);
    }
}
