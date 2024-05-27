package deco2800.thomas.items;

/**
 * Sword class allows to create an instance of sword
 */
public class Sword extends Weapon {
    /**
     * Constructor of sword
     * @param itemName the name of sword
     * @param weight the weight of sword
     */
    public Sword(String itemName, String weaponId, double weight, String texture) {
        super(itemName, weight, texture);
        this.setWeaponType("Melee");
        setItemId(weaponId);
    }

    /**
     * Constructor of sword
     * @param itemName the name of sword
     * @param weight the weight of sword
     * @param damage the damage of sword
     * @param attackRange the attacking range of sword
     * @param attackSpeed the attack speed of sword
     */
    public Sword(String itemName, String weaponId, double weight, double damage, double attackRange, double attackSpeed, String texture) {
        super(itemName, weight,damage,attackRange, attackSpeed, texture);
        this.setWeaponType("Melee");
        setItemId(weaponId);
    }
}
