package deco2800.thomas.entities.enemies.desert;

import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.Projectile;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.SpriteSheetAnimation;

public class DesertBossMinionPeon extends EnemyPeon {

    private float aggressionRange;
    private float attackSpeed;
    private float attackCooldown;
    private float attackRange;

    /**
     * Constructor for a DesertBossMinionPeon.
     * @param row Row to spawn the DesertBossMinionPeon on.
     * @param col Column to spawn the DesertBossMinionPeon on.
     */
    public DesertBossMinionPeon (float row, float col) {
        // Use 0.2 as the speed for this enemy
        super(row, col, 0.15f, 10, 8f, 1f, 0.75f, 20);
        this.setObjectName("desertBossMinionEnemy");
        SpriteSheetAnimation animation = new SpriteSheetAnimation("desert_boss_minion_spritesheet", 0.1f);
        this.setAnimation(animation);
        this.attackCooldown = 0;
    }

    /**
     * Launches an attack towards the specified Entity.
     * @param target Entity to launch an attack towards.
     */
    @Override
    public void attack(Peon target) {
        Projectile p = new Projectile(this.getCol(), this.getRow(),
                Projectile.getDirectionTo(this, target), 20f, 1, 1/4f, target);
        p.setTexture("web_projectile");
        GameManager.get().getWorld().addEntity(p);
    }
}