package deco2800.thomas.entities.enemies.swamp;

import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.Projectile;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.ScreenManager;
import deco2800.thomas.managers.SpriteSheetAnimation;

public class SwampBossPeon extends EnemyPeon {

    private float aggressionRange;
    private float attackSpeed;
    private float attackCooldown;
    private float attackRange;

    /**
     * Constructor for a SwampBossPeon.
     * @param row Row to spawn the SwampBossPeon on.
     * @param col Column to spawn the SwampBossPeon on.
     */
    public SwampBossPeon (float row, float col) {
        // Use 0.05 as the speed for this enemy
        super(row, col, 0.05f, 100, 7, 4, 0.5f, 60);
        this.setObjectName("swampBossEnemy");
        SpriteSheetAnimation animation = new SpriteSheetAnimation("swamp_boss_spritesheet",
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
        float direction = Projectile.getDirectionTo(this, target);
        float col = this.getCol();
        float row = this.getRow();

        Projectile p1 = new SwampProjectile(col, row,
                direction, 20f, 3, 2f, target);

        Projectile p2 = new SwampProjectile((float) Math.cos(direction) + col, -1 * (float) Math.sin(direction) + row,
                direction, 20f, 3, 2f, target);

        Projectile p3 = new SwampProjectile(-1 * (float) Math.cos(direction) + col, (float) Math.sin(direction) + row,
                direction, 20f, 3, 2f, target);

        GameManager.get().getWorld().addEntity(p1);
        GameManager.get().getWorld().addEntity(p2);
        GameManager.get().getWorld().addEntity(p3);
    }

    public String getName(){
        return "swampBossEnemy";
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