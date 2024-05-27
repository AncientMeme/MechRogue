package deco2800.thomas.entities.friendlyNPCs;

import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.RenderConstants;
import deco2800.thomas.managers.SoundManager;
import deco2800.thomas.tasks.AbstractTask;
import deco2800.thomas.tasks.WanderTask;
import deco2800.thomas.ui.TextBubble;

import java.util.Random;

/**
 * Generic FriendlyPeon class that FriendlyNPCs inherit from
 *
 * FriendlyPeons are interactable and present dialogue when clicked upon
 *
 */
public class FriendlyPeon extends Peon {

    //NPCs dialogue
    //TODO: Create an XML dialogue tree and update dialogue from it
    private String greeting = "Hello there weary traveller! How goes thee?";

    //String for the icon that appears next to the textbubble
    private String npcFace = "VillagerFace";

    String textColor = "GRAY";

    private transient AbstractTask task;

    // Used to optimise constant checking of all entities distance
    private int ticksSinceLastChecked = 0;

    private String dialoguePath = "resources/dialogue_trees/DefaultDialogue.txt";

    /**Constructor*/
    public FriendlyPeon(float row, float col, float speed, int health) throws
            IllegalArgumentException {
        super(row, col, speed, health);
        this.setObjectName("FriendlyPeon");
        this.setRenderOrder(RenderConstants.ENEMY_PEON_RENDER);
        this.setTexture("spacman_blue");
    }


    @Override
    public void onTick(long i) {
        if (task != null && task.isAlive()) {
            task.onTick(i);
            if (task.isComplete()) {
                task = null;
            }
        } else {
            ticksSinceLastChecked++;
            if (ticksSinceLastChecked >= 100) {
                ticksSinceLastChecked = 0;
                this.task = (new WanderTask(this, 2f));
            }
        }

    }

    /**
     * TODO: implement a dialogue tree to update dialogue with
     */
    public void nextDialogueBranch() {};

    /**
     * Getter for npcFace
     *
     * @return String that references face texture in map
     */
    public String getPeonFace() {
        return npcFace;
    }

    /**
     * Getter for textColor
     *
     */
    public String getTextColor() {
        return textColor;
    }

    /**
     * Setter for textColor
     *
     */
    public void setTextColor(String color) {
        this.textColor = color;
    }

    /**
     * Setter for npcFace
     *
     * @param texture String that references face texture in map
     */
    public void setPeonFace(String texture) {
        this.npcFace = texture;
    }


    public void setDialoguePath(String dialoguePath) {
        this.dialoguePath = dialoguePath;
    }

    /**
     * Getter for npcFace
     *
     * @return String that contains the npcs unique dialogue
     */
    public String getGreeting() {
        return greeting;
    }

    /**
     * Setter for greeting
     *
     * @param greeting String that contains the npcs unique dialogue
     */
    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    protected static final Random randomBgm = new Random();

    protected String getBgmPath() {
        return null;
    }

    public void playBgm() {
        if (getBgmPath() != null) {
            SoundManager.get().playSound(getBgmPath());
        }
    }

    public String getDialoguePath() {
        return dialoguePath;
    }

}


