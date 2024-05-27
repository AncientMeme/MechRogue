package deco2800.thomas.entities.enemies.tundra;

import deco2800.thomas.entities.*;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.SpriteSheetAnimation;

public class TundraRangedPeon extends EnemyPeon {

    /**
     * Constructor for the Tundra Ranged Peon. Makes a tundra ranged enemy as the specific coordinates
     * @param row Row to spawn the TundraRangedPeon on.
     * @param col Column to spawn the TundraRangedPeon on.
     */
    public TundraRangedPeon (float row, float col) {
        // Use 0.05 as the speed for this enemy
        super(row, col, 0.025f, 30, 10, 7.5f, 0.2f, 20);
        this.setObjectName("tundraRangedEnemy");
        SpriteSheetAnimation animation = new SpriteSheetAnimation("tundra_ranged_spritesheet",
                0.325f);
        this.setAnimation(animation);
    }

    /**
     * Launches an attack towards the specified Entity.
     * @param target Entity to launch an attack towards.
     */
    @Override
    public void attack(Peon target) {
        GameManager.get().getWorld().addEntity(new BlindnessHomingProjectile(this.getCol(),
                this.getRow(), 0, 3f, 3, 2.5f, target));
    }
}