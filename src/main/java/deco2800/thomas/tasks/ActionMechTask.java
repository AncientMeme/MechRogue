package deco2800.thomas.tasks;

import deco2800.thomas.entities.AgentEntity;
import deco2800.thomas.entities.MechState;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.items.DroppedItem;
import deco2800.thomas.items.MechSuit;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.renderers.PauseMenuRenderer;

public class ActionMechTask extends AbstractTask {
    // member is static as there can only be one unequipped mech in the game
    // we want to keep track of this mech.
    private static DroppedItem item;

    private boolean enter;
    private boolean complete;
    private boolean taskAlive = true;
    private PlayerPeon entity;

    public ActionMechTask(AgentEntity entity, boolean enter) {
        super(entity);
        this.entity = (PlayerPeon) entity;
        this.enter = enter;
        this.complete = false;
    }


    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public boolean isAlive() {
        return taskAlive;
    }

    @Override
    public void onTick(long tick) {

        complete = false;
        if (PauseMenuRenderer.isPaused()) {
            return;
        }
        //Remove the item from the world map
        if (enter) {
            if (item != null) {
                item.dispose();
            }
            item = GameManager.get()
                    .getWorld()
                    .getWorldItems()
                    .getItem((int)entity.getRow(), (int)entity.getCol(), "Mech");
            GameManager.get().getWorld().getWorldItems().removeItem(
                    (int) entity.getRow(), (int) entity.getCol(), item);
            entity.setTexture("Mech_001");
        } else {
            entity.setTexture("spacman_ded");// left mech
            if (entity.getMechState() == MechState.MECH_BROKE) {
                item.dispose();
                item = new DroppedItem((int)entity.getRow(), (int)entity.getCol(), 1,
                        new MechSuit(1, "mech_breakdown_still"));
            } else {
                item.dispose();
                item = new DroppedItem((int)entity.getRow(), (int)entity.getCol(), 1,
                        new MechSuit(1, "Mech_001"));
            }
            GameManager.get().getWorld().getWorldItems().addItem((int)entity.getRow(), (int)entity.getCol(), item);
            GameManager.get().getWorld().addStaticEntity(item);
        }
        complete = true;
    }
}
