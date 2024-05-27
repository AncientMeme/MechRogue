package deco2800.thomas.tasks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import deco2800.thomas.entities.AgentEntity;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.items.DroppedItem;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.renderers.PauseMenuRenderer;
import deco2800.thomas.worlds.AbstractWorld;

/**
 * Task responsible for picking up items at the players location.
 */
public class PlayerPickupItemTask extends AbstractTask {
    DroppedItem item;

    private boolean complete;
    private boolean taskAlive = true;
    private PlayerPeon entity;

    public PlayerPickupItemTask(AgentEntity entity, DroppedItem item) {
        super(entity);
        this.entity = (PlayerPeon) entity;
        this.item = item;
        this.complete = false;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isAlive() {
        return taskAlive;
    }


    public void onTick(long tick) {
        if (PauseMenuRenderer.isPaused()) {
            return;
        }
        //Remove the item from the world map
        GameManager.get().getWorld().getWorldItems().removeItem((int) entity.getRow(), (int) entity.getCol(), item);

        //Add the item to the player's inventory
        entity.getPlayerInventory().addItem(item.getItem());
        //Dispose the renderable
        item.dispose();
        complete = true;
    }
}

