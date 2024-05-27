package deco2800.thomas.items;

import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.items.UnrenderedItem;
import deco2800.thomas.renderers.InventoryRenderer;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**v
Author: @samnolan4
Inventory used to store player's items.
Could also be used for enemies' items.
 */
public class Inventory implements Iterable<UnrenderedItem>, Serializable {

    /*
    Storing the player's items in a LinkedList for now.
    May want to change depending on the uses of the inventory.
    */
    private LinkedList<UnrenderedItem> inventory;
    private int num = 0;//number of items in the inventory

    public Inventory(LinkedList<UnrenderedItem> inventory){
        this.inventory = inventory;
    }

    public Inventory(){
        this.inventory = new LinkedList<>();
    }

    public void addItem(UnrenderedItem item){
        this.inventory.add(item);
        num += 1;
    }

    public void removeItem(UnrenderedItem item){

        inventory.remove(item);
        num -= 1;
    }

    public UnrenderedItem getItem(int index) {
        return inventory.get(index);
    }

    public void removeItem(String string){
        for(UnrenderedItem item: this.inventory){
            if(item.getItemName().equals(string)){
                this.removeItem(item);
                break;
                /*Sam, your code previously missed a break here, it may cause error in the linked list
                 * because you access the list with an iterator, and you remove the element of the list
                 * before terminating the loop, that would cause a concurrentModificationException
                 */
            }
        }
    }

    @Override
    public Iterator<UnrenderedItem> iterator() {
        return inventory.iterator();
    }


    /**
     * Checks inventory for given item
     * @param itemName name of item being searched for
     * @return true if inventory contains item, false otherwise.
     */
    public boolean contains(String itemName) {
        for (UnrenderedItem inventoryItem : this.inventory) {
            if (inventoryItem.getItemName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }
    public int size() {
        return inventory.size();
    }
    public int numOfItems() {
        return num;
    }
}
