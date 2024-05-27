package deco2800.thomas.items;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Stores the Items in a HashMap representation of the Game Map.
 */
public class WorldItems implements Serializable {
    private HashMap<Integer, HashMap<Integer, LinkedList<DroppedItem>>> worldItems;

    public WorldItems(){
        worldItems = new HashMap<Integer, HashMap<Integer, LinkedList<DroppedItem>>>();

    }

    /**
     * Adds Item to the map
     * @param row Tile row of the map to add to
     * @param col Tile column of the map to add to
     * @param item Item to be added
     */
    public void addItem(int row, int col, DroppedItem item){
        if(!worldItems.containsKey(row)){
            worldItems.put(row, new HashMap<Integer, LinkedList<DroppedItem>>());
            worldItems.get(row).put(col, new LinkedList<DroppedItem>());
            worldItems.get(row).get(col).add(item);
        }
        else{
            if(!worldItems.get(row).containsKey(col)){
                worldItems.get(row).put(col, new LinkedList<DroppedItem>());
                worldItems.get(row).get(col).add(item);
            }
            else{
                worldItems.get(row).get(col).add(item);
            }
        }
    }

    /**
     * Removes the Item from the map if it exists there
     * @param row row to remove the item from
     * @param col column to remove the item from
     * @param item item to be removed
     * @return true if the item existed at that spot.
     */
    public boolean removeItem(int row, int col, DroppedItem item){
        if(worldItems.containsKey(row)){
            if(worldItems.get(row).containsKey(col)){
                return worldItems.get(row).get(col).remove(item);
            }
        }
        return false;
    }

    /**
     * Retrieves a list of items at a location.
     * @param row the row to retrieve the item list from
     * @param col the column to retrieve the item list from
     * @return the list of items at this location
     */
    public LinkedList<DroppedItem> getItems(int row, int col){
        if(worldItems.containsKey(row)){
            if(worldItems.get(row).containsKey(col)){
                return worldItems.get(row).get(col);
            }
        }
        return new LinkedList<>();
    }

    public DroppedItem getItem(int row, int col, String itemName) {
        LinkedList<DroppedItem> items = getItems(row, col);
        for (DroppedItem item : items) {
            if (item.getItem().getItemName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }
}
