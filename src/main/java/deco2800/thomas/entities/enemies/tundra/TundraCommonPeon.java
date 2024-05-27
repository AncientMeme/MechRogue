package deco2800.thomas.entities.enemies.tundra;

import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.Projectile;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.SpriteSheetAnimation;

public class TundraCommonPeon extends EnemyPeon {

    /**
     * Constructor for a TundraCommonPeon.
     * @param row Row to spawn the TundraCommonPeon on.
     * @param col Column to spawn the TundraCommonPeon on.
     */
    public TundraCommonPeon (float row, float col) {
        // Use 0.05 as the speed for this enemy
        super(row, col, 0.065f, 30, 8, 1f,2f, 20);
        this.setObjectName("tundraCommonEnemy");
        SpriteSheetAnimation animation = new SpriteSheetAnimation("tundra_common_spritesheet",
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
                20f, 1, this.getAttackRange()/20f, target, 0.5f, 0.25f);
        p.setTexture("slash_effect");
        GameManager.get().getWorld().addEntity(p);
    }
}
