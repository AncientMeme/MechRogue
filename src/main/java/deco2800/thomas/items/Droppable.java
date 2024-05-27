package deco2800.thomas.items;

/**
 * Droppable interface defines the behaviour of items which can be dropped from inventory
 */
public interface Droppable {

    /**
     * determine whether an item has been dropped
     * @return whether the item was dropped
     */
    public boolean isDropped();

    /**
     * drop an item from inventory
     */
    public void drop();
}
