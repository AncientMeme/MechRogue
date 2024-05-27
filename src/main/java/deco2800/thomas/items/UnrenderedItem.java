package deco2800.thomas.items;

import java.io.Serializable;

/**
 * Author: @jason793259295
 * UnrenderedItem class is abstract class, all instances of it represent the items that
 * will not be rendered in the world including weapons, orbs and something else.
 * They will be interacted with inventory.
 */
public abstract class UnrenderedItem implements Serializable {
    private String itemType;
    private String itemName;
    private String itemId;
    private double weight;
    private String texture;

    /**
     * Constructor of UnrenderedItem
     * @param weight the weight of items, an inventory has a limit on weight of items carried
     */
    public UnrenderedItem(double weight, String texture){
        this.weight = weight;
        this.texture = texture;
    }

    /**
     * get this item's type
     * @return item's type
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * set the item's type
     * @param itemType the type of this item
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * get this item's name
     * @return the name of this item
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * set the item's name
     * @param itemName item's name
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemId() {return itemId; }

    public void setItemId(String itemId) {this.itemId = getItemType()+ "_"+itemId; }

    /**
     * get the weight of item
     * @return item's weight
     */
    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Item Name: " + getItemName() + "\n" +
                "Item Type: " + getItemType() + "\n" +
                "Item Id: " + getItemId() + "\n";
    }

    public String getTexture(){
        return texture;
    }
}
