package deco2800.thomas.entities.enemies;

import deco2800.thomas.entities.AbstractEntity;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.RenderConstants;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.tasks.*;

import java.util.List;

/**
 * Generic EnemyPeon class that specific enemies inherit from
 *
 * TODO: make a way for each EnemyPeon to have an animation for every task it wants to do eg. "walking", "attacking"
 * "idle" and then come up with a clever solution to switch between these animations. Might be worth implementing
 * it in Peon or AgentEntity to make it easier to have standard animations.
 *
 * Animation code:
 * SpriteSheetAnimation animation = new SpriteSheetAnimation("tundra_common_spritesheet", 0.1f);
 * this.setAnimation(animation);
 */
public class EnemyPeon extends Peon {

    /**
     * Enemy Peon constructor
     */
    private float aggressionRange;
    private float attackSpeed;
    private float attackRange;

    // The cooldown rate till this EnemyPeon can attack in ms
    private float attackCooldown = 0;

    // The current task for this EnemyPeon
    private transient AbstractTask task;

    // Cooldown for damage tint
    private float damagedTintCooldown = 0;

    // Used to optimise constant checking of all entities distance
    private int ticksSinceLastChecked = 0;

    // Amount of experience gained by player when enemy is killed
    private int experience;


    /**
     * Constructor for an EnemyPeon
     * @param row Row to spawn the EnemyPeon on.
     * @param col Column to spawn the EnemyPeon on.
     * @param speed Movement Speed of the EnemyPeon. (Possibly remove from constructor)
     * @param health Health of the EnemyPeon. (Possibly remove from constructor.
     * @param aggressionRange The range at which the enemy becomes aggressive towards its targets.
     * @param attackRange The range from which the enemy can attack its target.
     * @param attackSpeed The attack speed of the enemyPeon, expressed in attacks per second
     *                    (assuming 60 ticks per second)
     */
    public EnemyPeon(float row, float col, float speed, int health, float aggressionRange, float attackRange,
                     float attackSpeed, int experience) {
        super(row, col, speed, health);
        this.experience = experience;
        this.setRenderOrder(RenderConstants.ENEMY_PEON_RENDER);
        this.setTexture("dummy_enemy");

        //TODO: check if attackRange is greater than the aggressionRange and throw an error.
        this.aggressionRange = aggressionRange;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;

    }

    /**
     * Death handler for enemy
     */
    @Override
    public void handleDeath() {
        getDamageIndicators().forEach(indicator -> indicator.getLabel().setVisible(false));
        // Delete entity from the world
        this.dispose();
    }


    /**
     * Enemy on tick method.
     * Steps AI code
     * @param i
     */
    public void onTick(long i) {
        if (damagedTintCooldown > 0) {
            damagedTintCooldown -= 1;
            this.setColor(1,0.4f,0.4f,1);
        } else if (damagedTintCooldown == 0) {
            this.setColor(1,1,1,1);
        }

        this.setAttackCooldown(attackCooldown - 1/60f);
        if (task != null && task.isAlive()) {
            task.onTick(i);
        } else {
            ticksSinceLastChecked++;
            if (ticksSinceLastChecked >= 30) {
                ticksSinceLastChecked = 30;
                List<AbstractEntity> entities = GameManager.get().getWorld().getEntities();
                for (AbstractEntity e : entities) {
                    if (this.isValidTarget(e)) {
                        this.task = new AttackTask(this, (Peon) e);
                    }
                }
            }
        }
        tickBuffs();
        tickIndicators();
    }

    /**
     * Launches an attack towards the specified Entity.
     * To be overridden in subclasses
     * @param target Entity to launch an attack towards.
     */
    public void attack(Peon target) {
        //
    }

    /**
     * Sets the current target of the enemy.
     * @param target target to set the enemy to.
     */
    public void setTarget(Peon target) {
        this.setTask(new AttackTask(this, target));
    }

    /**
     * Checks if the target entity is a valid target for this EnemyPeon. A target is valid if it is a PlayerPeon
     * and within the aggression range of the enemy.
     * @param target Entity to check is a valid target.
     * @return true when the target is valid, false if not.
     */
    public boolean isValidTarget(AbstractEntity target) {
        return target != null && target.getObjectName().equals("playerPeon") && isWithinAggressionRange((Peon) target);
    }

    /**
     * Checks if the target entity is in the aggression range of this EnemyPeon.
     * @param target entity to check is in the aggression range
     * @return true if the target is in the aggression range, false if not
     */
    public boolean isWithinAggressionRange(Peon target) {
        return target != null && this.distance(target) <= aggressionRange;
    }

    /**
     * Adds or subtracts health from this EnemyPeon
     * @param amount a positive or negative integer
     */
    public void addHealth(int amount) {
        super.addHealth(amount);
        if (amount < 0) {
            damagedTintCooldown = 7;
        }
    }

    /**
     * Checks whether an entity is within this EnemyPeon's attack range
     * @param target the entity to check whether it is within attack range
     * @return true iff target is within attack range, else false
     */
    public boolean isWithinAttackRange(Peon target) {
        return target !=null && this.distance(target) <= attackRange;
    }

    /**
     * Returns the aggression range of this EnemyPeon
     * @return aggressionRange float variable
     */
    public float getAggressionRange() {
        return aggressionRange;
    }

    /**
     * Sets the aggression range of this EnemyPeon
     * @param aggressionRange the float range to use
     */
    public void setAggressionRange(float aggressionRange) {
        this.aggressionRange = aggressionRange;
    }

    /**
     * Returns the attack speed of this EnemyPeon
     * @return attackSpeed float variable
     */
    public float getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * Sets the attack speed of this EnemyPeon
     * @param attackSpeed the attackSpeed to use
     */
    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    /**
     * Returns the attack cooldown of this EnemyPeon
     * @return attackCooldown float variable
     */
    public float getAttackCooldown() {
        return attackCooldown;
    }

    /**
     * Sets the cooldown time for this EnemyPeon to attack at
     * @param attackCooldown the cooldown time in ms to
     */
    public void setAttackCooldown(float attackCooldown) {
        this.attackCooldown = Math.max(attackCooldown, 0);
    }

    /**
     * Returns the attack range of this EnemyPeon
     * @return attackRange float variable
     */
    public float getAttackRange() {
        return attackRange;
    }

    /**
     * Sets the attack range for this Enemy Peon
     * @param attackRange the attack range
     */
    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    /**
     * Returns amount of experience gained by killing this enemy
     * @return experience of enemy
     */
    public int getExperience() {
        return experience;
    }
}
