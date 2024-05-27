package deco2800.thomas.items;
import deco2800.thomas.entities.StaticEntity;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.worlds.Tile;

/**
Author: @samnolan4
Dropped Item is an item on the ground. It extends
AbstractEntity because it will have a renderable position on the map.
The underlying Item is stored as an instance member.
 */
public class DroppedItem extends StaticEntity {
    //The underlying item;
    UnrenderedItem item;
    private static final String ENTITY_ID_STRING = "item";


    public DroppedItem(UnrenderedItem item) {
        setCollidable(false);
        this.item = item;
        this.setTexture(item.getTexture());
    }

    public DroppedItem(int row, int col, int renderOrder, UnrenderedItem item){
        super(GameManager.get().getWorld().getTile(col, row), renderOrder, item.getTexture(), false);
        setCollidable(false);
        this.item = item;
        this.setObjectName(ENTITY_ID_STRING);
    }

    public UnrenderedItem getItem(){
        return item;
    }

    @Override
    public void onTick(long i) {
        //unused
    }
}