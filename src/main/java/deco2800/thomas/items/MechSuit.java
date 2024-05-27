package deco2800.thomas.items;

/**
 * Mech suit in the game
 */
public class MechSuit extends UnrenderedItem {
    /**
     * Constructor of UnrenderedItem
     *
     * @param weight the weight of items, an inventory has a limit on weight of items carried
     */
    public MechSuit(double weight, String texture) {
        super(weight, texture);
        this.setItemName("Mech");
        this.setItemType("Mech");
        this.setItemId("001");
    }
}
