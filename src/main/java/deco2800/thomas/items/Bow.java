package deco2800.thomas.items;

/**
 * Bow class allows to construct an instance of bow
 */
public class Bow extends Weapon {
    /**
     * Constructor of Bow
     * @param itemName the name of bow
     * @param weight the weight of bow
     */
    public Bow(String itemName, String weaponId, double weight, String texture) {
        super(itemName, weight, texture);
        this.setWeaponType("Range");
        setItemId(weaponId);
    }

    /**
     * Constructor of Bow
     * @param itemName the name of bow
     * @param weight the weight of bow
     * @param damage the damage of bow
     * @param attackRange the attacking range of bow
     * @param attackSpeed the attack speed of bow
     */
    public Bow(String itemName, String weaponId, double weight, double damage, double attackRange, double attackSpeed, String texture) {
        super(itemName, weight, damage, attackRange, attackSpeed, texture);
        this.setWeaponType("Range");
        setItemId(weaponId);
    }
}
