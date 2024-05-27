package deco2800.thomas.items;

public class Potion extends UnrenderedItem {

    /**
     * Constructor of UnrenderedItem
     *
     * @param weight the weight of items, an inventory has a limit on weight of items carried
     */
    public Potion(String itemName, String itemId, double weight, String texture) {
        super(weight, texture);
        this.setItemType("potion");
        this.setItemName(itemName);
        setItemId(itemId);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
