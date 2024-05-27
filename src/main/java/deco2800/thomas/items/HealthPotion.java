package deco2800.thomas.items;

public class HealthPotion extends Potion {
    private int healingAmount = 0;
    /**
     * Constructor of UnrenderedItem
     *
     * @param itemName
     * @param itemId
     * @param weight   the weight of items, an inventory has a limit on weight of items carried
     * @param texture
     */
    public HealthPotion(String itemName, String itemId, double weight, String texture, int healing) {
        super(itemName, itemId, weight, texture);
        setHealingAmount(healing);
    }

    public void setHealingAmount(int healing) {
        this.healingAmount = healing;
    }

    public int getHealingAmount() {
        return healingAmount;
    }
}
