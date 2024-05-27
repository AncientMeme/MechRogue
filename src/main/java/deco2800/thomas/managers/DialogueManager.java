package deco2800.thomas.managers;

import deco2800.thomas.entities.friendlyNPCs.FriendlyPeon;
import deco2800.thomas.ui.TextBubble;
import deco2800.thomas.util.DialogueParser;


/**
 * Class to manage the interaction between entities and the dialogue bubble
 *
 * Uses singleton designer pattern
 * */
public class DialogueManager {

    /*Single instance of DM*/
    private static DialogueManager instance = null;

    /*TextBubble UI*/
    TextBubble bubble;

    /*Last NPC interacted with. If the same then close the dialogue bubble*/
    FriendlyPeon lastNPC = null;

    /*Current dialogue parser*/
    DialogueParser dP = null;



    /**
     * Returns an instance of the DM
     *
     * @return DialogueManager
     */
    public static DialogueManager get() {
        if (instance == null) {
            // create a new gameManager
            instance = new DialogueManager();
        }
        return instance;
    }

    /**
     * Private constructor to enforce use of get()
     */
    private DialogueManager() {
    }

    /**
     * Reads the peons dialogue tree
     *
     * @param path path to the dialogue tree
     */
    public void startDialogue(String path) {
        dP = new DialogueParser(path);
        String[] dialogue = dP.getDialogue();
        addDialogue(dialogue);
    }

    public void choose(Integer choice) {
        if (dP != null) {
            if(dP.isEnd()) {
                dP.getNextBranch(choice);
                String[] dialogue = dP.getDialogue();
                addDialogue(dialogue);
            } else {
                String buff = dP.isThereBuff();
                if (!buff.equals("none")) {
                    System.out.println("no buffs"); //ToDo: Add buff
                }
                bubble.dispose();
                dP = null;
            }
        }
    }

    /**
     * Get TextBubble
     */
    public TextBubble getTextBubble() {
        return bubble;
    }

    /**
     * Add TextBubble
     */
    public void addTextBubble(TextBubble textBubble) {
        this.bubble = textBubble;
    }

    /**
     * Add Dialogue
     */
    public void addDialogue(String[] dialogue) {
        this.bubble.setSpeech(dialogue);
    }

    /**
     * Get Previous NPC
     */
    public FriendlyPeon getLastNPC() {
        return lastNPC;
    }

    /**
     * Get Previous NPC
     */
    public void setLastNPC(FriendlyPeon npc) {
        this.lastNPC = npc;
    }

    /**
     * Helper method to avoid NPException when comparing last NPC and current NPC
     */
    public static boolean isEqual(Object o1, Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }

}
