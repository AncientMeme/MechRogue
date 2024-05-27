package deco2800.thomas.entities.enemies.desert;

import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.Projectile;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.ScreenManager;
import deco2800.thomas.managers.SpriteSheetAnimation;

public class DesertBossPeon extends EnemyPeon {

    private float aggressionRange;
    private float attackSpeed;
    private float attackCooldown;
    private float attackRange;
    private int specialAttackCountdown;

    /**
     * Constructor for a DesertBossPeon.
     * @param row Row to spawn the DesertBossPeon on.
     * @param col Column to spawn the DesertBossPeon on.
     */
    public DesertBossPeon (float row, float col) {
        // Use 0.05 as the speed for this enemy
        super(row, col, 0.05f, 100, 8, 7, 0.5f, 50);
        this.setObjectName("desertBossEnemy");
        SpriteSheetAnimation animation = new SpriteSheetAnimation("desert_boss_spritesheet",
                0.1f);
        this.setAnimation(animation);
        this.attackCooldown = 0;
        this.specialAttackCountdown = 3;
    }

    /**
     * Launches an attack towards the specified Entity.
     * @param target Entity to launch an attack towards.
     */
    @Override
    public void attack(Peon target) {
        if (this.specialAttackCountdown == 0) {
            this.specialAttackCountdown = 2;
            // For some reason, the getRow and getCol methods give opposite results than expected.
            GameManager.get().getWorld().addEntity(new DesertBossMinionPeon(this.getCol() + 1f, this.getRow()));
            GameManager.get().getWorld().addEntity(new DesertBossMinionPeon(this.getCol(), this.getRow()));
        } else {
            WebProjectile p = new WebProjectile(this.getCol(), this.getRow(),
                    Projectile.getDirectionTo(this, target), 6f, 2, 0.5f, target);
            GameManager.get().getWorld().addEntity(p);
            this.specialAttackCountdown -= 1;
        }
    }

    public String getName(){
        return "desertBossEnemy";
    }

    /**
     * handle boss death to game
     */
    @Override
    public void handleDeath() {
        ScreenManager.get().getCurrentScreen().bossHandleDeath(getName(),getPosition());
        super.handleDeath();
    }

}