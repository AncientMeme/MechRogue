package deco2800.thomas.entities.friendlyNPCs;

import deco2800.thomas.managers.SpriteSheetAnimation;

public class RogueNPC extends FriendlyPeon{

    /*Unique greeting of Wizard*/
    String greeting = "Attention Mr. Underhill!";

    /**
     * Constructor for a RogueNPC
     * @param row Row to spawn the NPC
     * @param col Column to spawn the NPC
     * @param speed Speed of the NPC
     * @param health Health of the NPC
     */
    public RogueNPC(float row, float col, float speed, int health) throws
            IllegalArgumentException {
        super(row, col, speed, health);
        this.setObjectName("RogueNPC");

        SpriteSheetAnimation animation = new SpriteSheetAnimation("Rogue",
                0.1f);

        this.setAnimation(animation);
        this.setGreeting(greeting);
        this.setPeonFace("RogueFace");
        this.setTextColor("RED");
        this.setDialoguePath("resources/dialogue_trees/Rogue.txt");
    }
    @Override
    protected String getBgmPath() {
        return "Rogue_Greeting" + (randomBgm.nextInt() % 2 == 0 ? "1" : "2") + ".mp3";
    }
}


