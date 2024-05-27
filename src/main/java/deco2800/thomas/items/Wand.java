package deco2800.thomas.items;

/**
 * Wand class allows construct of wand
 */
public class Wand extends Weapon{

    public Wand(String itemName, String weaponId, double weight, String texture){
        super(itemName, weight, texture);
        this.setWeaponType("Magic");
        setItemId(weaponId);
    }

    /**
     * Constructor of Wand
     * @param itemName the name of sword
     * @param weight the weight of sword
     * @param damage the damage of sword
     * @param attackRange the attacking range of sword
     * @param attackSpeed the attack speed of the weapon
     */
    public Wand(String itemName, String weaponId, double weight, double damage, double attackRange, double attackSpeed, String texture){
        super(itemName, weight, damage, attackRange, attackSpeed, texture);
        this.setWeaponType("Magic");
        setItemId(weaponId);
    }
}
