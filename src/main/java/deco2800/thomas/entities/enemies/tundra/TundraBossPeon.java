package deco2800.thomas.entities.enemies.tundra;

import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.Projectile;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.ScreenManager;
import deco2800.thomas.managers.SpriteSheetAnimation;

public class TundraBossPeon extends EnemyPeon {

    /**
     * Constructor for a TundraBossPeon.
     * @param row Row to spawn the TundraBossPeon on.
     * @param col Column to spawn the TundraBossPeon on.
     */
    public TundraBossPeon (float row, float col) {
        // Use 0.05 as the speed for this enemy
        super(row, col, 0.05f, 100, 2, 3, 0.5f, 90);
        this.setObjectName("tundraBossEnemy");
        SpriteSheetAnimation animation = new SpriteSheetAnimation("tundra_boss_spritesheet",
                0.1f);
        this.setAnimation(animation);
    }

    /**
     * Launches an attack towards the specified Entity.
     * @param target Entity to launch an attack towards.
     */
    @Override
    public void attack(Peon target) {
        float[] directions = {0, 1, 2, 3, 4, 5, 6, 7};
        for (int i = 0; i < 8; i++) {
            Projectile p = new FreezeMeleeProjectile(this.getCol(), this.getRow(), (float) (directions[i] * Math.PI * 0.25),
                    5f, 5, 1f, target, 2.5f, 0.5f);
            p.setTexture("slash_effect");
            GameManager.get().getWorld().addEntity(p);
        }
    }

    public String getName(){
        return "tundraBossEnemy";
    }

    /**
     * handle boss death to game
     */
    @Override
    public void handleDeath() {
        ScreenManager.get().getCurrentScreen().bossHandleDeath(getName(), getPosition());
        super.handleDeath();
    }
}
