package deco2800.thomas.entities;

import java.io.Serializable;

/**
 * Represents a mech that, when the player enters, increases their stats.
 */
public class Mech implements Serializable {
    //Mech Stats
    private final int strength;
    private final int defense;
    private final float speed;
    private final int maxHealth;
    private int health;

    /**
     * Mech constructor
     * @param strength - value that mech adds to player's strength
     * @param defense - value that mech adds to player's defense
     * @param speed - value that mech adds to player's speed
     * @param maxHealth - value that mech adds to player's maxHealth
     */
    public Mech(int strength, int defense, float speed, int maxHealth){
        this.strength = strength;
        this.defense = defense;
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    /**
     * Returns the mech's strength value that will be added onto the player's current strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Returns the mech's defense value that will be added onto the player's current defense
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Returns the mech's speed value that will be deducted onto the player's current speed
     */
    public float getSpeedDeduction() {
        return speed;
    }

    /**
     * Returns the mech's current health that is added onto the player's current health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the mech's maximum health that is added onto the player's maximum health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets the mech's current health
     * @param newHealth the mech's new current health value
     */
    public void setHealth(int newHealth) {
        if (newHealth < 0) {
            health = 0;
        } else if (newHealth > maxHealth) {
            health = maxHealth;
        } else {
            health = newHealth;
        }
    }
}
