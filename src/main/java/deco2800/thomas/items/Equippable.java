package deco2800.thomas.items;

/**
 * Equippable interface defines the behaviour of an item which can be equipped by player
 */
public interface Equippable {

    /**
     *
     * @return whether the item was equipped
     */
    public boolean isEquipped();

    /**
     * Equip an item
     */
    public void equip();

    /**
     * Unequip an item
     */
    public void unequip();
}
