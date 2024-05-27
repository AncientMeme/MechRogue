package deco2800.thomas.items;

/**
 * Crossbow class allows to construct an instance of crossbow
 */
public class Crossbow extends Weapon{
    /**
     * Constructor of Crossbow
     * @param itemName the name of crossbow
     * @param weight the weight of crossbow
     */
    public Crossbow(String itemName, String weaponId, double weight, String texture) {
        super(itemName, weight, texture);
        this.setWeaponType("Range");
        setItemId(weaponId);
    }

    /**
     * Constructor of Crossbow
     * @param weight the weight of crossbow
     * @param damage the damage of crossbow
     * @param attackRange the attacking range of crossbow
     * @param attackSpeed the speed the crossbow can be used
     */
    public Crossbow(String itemName, String weaponId, double weight, double damage, double attackRange, double attackSpeed, String texture){
        super(itemName, weight, damage, attackRange, attackSpeed, texture);
        this.setWeaponType("Range");
        setItemId(weaponId);
    }
}
