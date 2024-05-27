package deco2800.thomas.entities.enemies.volcano;

import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.Projectile;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.ScreenManager;
import deco2800.thomas.managers.SpriteSheetAnimation;

public class VolcanoBossPeon extends EnemyPeon {

    private float aggressionRange;
    private float attackSpeed;
    private float attackCooldown;
    private float attackRange;

    /**
     * Constructor for a VolcanoBossPeon.
     * @param row Row to spawn the VolcanoBossPeon on.
     * @param col Column to spawn the VolcanoBossPeon on.
     */
    public VolcanoBossPeon (float row, float col) {
        // Use 0.05 as the speed for this enemy
        super(row, col, 0.05f, 100, 10, 10, 0.5f, 80);
        this.setObjectName("volcanoBossEnemy");
        SpriteSheetAnimation animation = new SpriteSheetAnimation("volcano_boss_spritesheet",
                0.1f);
        this.setAnimation(animation);
        this.attackCooldown = 0;
    }

    /**
     * Launches an attack towards the specified Entity.
     * @param target Entity to launch an attack towards.
     */
    @Override
    public void attack(Peon target) {
        FireballHomingProjectile p = new FireballHomingProjectile(this.getCol(), this.getRow(),
                Projectile.getDirectionTo(this, target), 5f, 2, 2.5f, target);
        p.setTexture("fireball");
        GameManager.get().getWorld().addEntity(p);
    }

    public String getName(){
        return "volcanoBossEnemy";
    }

    /**
     * handle boss death to game
     */
    @Override
    public void handleDeath() {
        System.out.println("boss death");
        ScreenManager.get().getCurrentScreen().bossHandleDeath(getName(),getPosition());
        super.handleDeath();
    }
}