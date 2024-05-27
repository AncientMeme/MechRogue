package deco2800.thomas.entities;

public interface HasHealth {
    /**
     * Returns the current health from peon.
     *
     * @return The current health of the peon
     * */
    int getHealth();

    /**
     * Returns the max health of peon.
     *
     * @return The max health of the peon
     * */
    int getMaxHealth();

    /**
     * Set the peon's health to the value entered. If the value exceeds max
     * health, then health will equal to max health. If the value is lower than
     * 0, then health will be set to 0.
     *
     * @param health Set the peon's health to the input value
     * */
    void setHealth(int health);

    /**
     * Returns the proportion of the peon's current health rounded to 2 decimal places
     *
     * @return The proportion of the peon's current health
     */
    double getProportionalHealth();


    /**
     * Set the peon's max health to the value entered. If health exceeds the new
     * max health, then health will equal to max health. Max health will be
     * 0 when the input value is less than 0
     *
     * @param maxHealth Set the peon's max health to the input value
     * */
    void setMaxHealth(int maxHealth);

    /**
     * Add health to the current health of peon. If the result exceeds max
     * health, then health will equal to max health. If the result is lower than
     * 0, then health will be set to 0.
     *
     * @param amount The amount of health to add, can be negative.
     * */
    void addHealth(int amount);

    /**
     * Function that can be overridden that is called when the peon is killed.
     * (when health <= 0)
     */
    void handleDeath();
}