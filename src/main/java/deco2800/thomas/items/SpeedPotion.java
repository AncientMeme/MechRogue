package deco2800.thomas.items;

public class SpeedPotion extends Potion{
    private int speedAmount = 0;
    /**
     * Constructor of UnrenderedItem
     *
     * @param itemName
     * @param itemId
     * @param weight   the weight of items, an inventory has a limit on weight of items carried
     * @param texture
     */
    public SpeedPotion(String itemName, String itemId, double weight, String texture, int speed) {
        super(itemName, itemId, weight, texture);
        setSpeedAmount(speed);
    }

    public void setSpeedAmount(int healing) {
        this.speedAmount = healing;
    }

    public int getSpeedAmount() {
        return speedAmount;
    }
}
