package deco2800.thomas.items;

/**
 * Axe class allows construct of axe instance of weapon
 */
public class Axe extends Weapon {

    public Axe(String itemName, String weaponId, double weight, String texture) {
        super(itemName, weight, texture);
        this.setWeaponType("Melee");
        setItemId(weaponId);
    }

    /**
     * Constructor of Axe
     * @param itemName the name of sword
     * @param weight the weight of sword
     * @param damage the damage of sword
     * @param attackRange the attacking range of sword
     * @param attackSpeed the attack speed of the weapon
     */

    public Axe(String itemName, String weaponId, double weight, double damage, double attackRange, double attackSpeed, String texture){
        super(itemName, weight, damage, attackRange, attackSpeed, texture);
        this.setWeaponType("Melee");
        setItemId(weaponId);
    }


}
