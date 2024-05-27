package deco2800.thomas.items;

/**
 * Tool kit in the game
 */
public class ToolKit extends UnrenderedItem implements Droppable {
    private boolean isDropped;

    public ToolKit(double weight) {
        super(weight, "tool_kit_001");
        this.setItemName("Tool Kit");
        this.setItemType("tool_kit");
        this.setItemId("001");
        isDropped = false;
    }

    /**
     * @return if item is dropped
     */
    @Override
    public boolean isDropped() {
        return isDropped;
    }

    /**
     * Drops item
     */
    @Override
    public void drop() {
        isDropped = true;
    }

    /**
     * picks up item
     */
    public void pickUp() {
        isDropped = false;
    }
}
