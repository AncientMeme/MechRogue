package deco2800.thomas.items;

public class Weapon extends UnrenderedItem implements Droppable, Equippable {
    private String weaponType;
    private double damage;
    private double attackRange;
    private double attackSpeed;
    private boolean isDropped;
    private boolean isEquipped;

    /**
     * Constructor of Weapon, set weapon's stats with default value
     * @param itemName the weapon's name
     * @param weight the weapon's weight
     */
    public Weapon(String itemName, double weight, String texture) {
        super(weight, texture);
        this.setItemType("weapon");
        this.setItemName(itemName);
        damage = 0;
        attackRange = 0;
        attackSpeed = 0;
        isDropped = false;
        isEquipped = false;
    }

    /**
     * Constructor of Weapon, customizing the weapon's stats initially
     * @param itemName weapon's name
     * @param weight weapon's weight
     * @param damage weapon's damage
     * @param attackRange weapon attacking range
     * @param attackSpeed weapon attack speed
     */
    public Weapon(String itemName, double weight, double damage, double attackRange, double attackSpeed, String texture) {
        super(weight, texture);
        this.setItemType("weapon");
        this.setItemName(itemName);
        this.damage = damage;
        this.attackRange = attackRange;
        this.attackSpeed = attackSpeed;
        isDropped = false;
        isEquipped = false;
    }

    /**
     * get weapon's type
     * @return weapon's type
     */
    public String getWeaponType() {
        return weaponType;
    }

    /**
     * get weapon's damage
     * @return weapon's damage
     */
    public double getDamage() {
        return damage;
    }

    /**
     * get weapon's attacking range
     * @return weapon's attacking range
     */
    public double getAttackRange() {
        return attackRange;
    }

    /**
     * get weapons attack speed
     * @return weapons attack speed
     */
    public double getAttackSpeed(){ return attackSpeed; }

    /**
     * get weapon's type, which can be melee or range
     * @param weaponType weapon's type
     */
    public void setWeaponType(String weaponType) {
        this.weaponType = weaponType;
    }

    /**
     * set weapon's damage
     * @param damage weapon's damage
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }

    /**
     * set the attacking range of a weapon
     * @param attackRange attacking range of weapon
     */
    public void setAttackRange(double attackRange) {
        this.attackRange = attackRange;
    }

    /**
     * set the attack speed of a weapon
     * @param attackSpeed
     */

    public void setAttackSpeed(double attackSpeed) { this.attackSpeed = attackSpeed; }

    @Override
    public String toString() {
        return super.toString() + "\n" +
                "Weapon Type: " + getWeaponType() + "\n" +
                "Attacking Range: " + getAttackRange() + "\n" +
                "Attacking Speed: " + getAttackSpeed() + "\n" +
                "Damage: " + getDamage();
    }

    @Override
    public boolean isDropped() {
        return isDropped;
    }

    @Override
    public void drop() {
        //unused
    }

    @Override
    public boolean isEquipped() {
        return isEquipped;
    }

    @Override
    public void equip() {
        isEquipped = true;
    }

    @Override
    public void unequip() {
        isEquipped = false;
    }
}
