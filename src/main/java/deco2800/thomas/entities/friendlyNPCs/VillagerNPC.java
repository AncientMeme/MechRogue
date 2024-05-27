package deco2800.thomas.entities.friendlyNPCs;

import deco2800.thomas.managers.SpriteSheetAnimation;

public class VillagerNPC extends FriendlyPeon{

    /*Unique greeting of Wizard*/
    String greeting = "Yo waddup homie";

    /**
     * Constructor for a VillagerNPC
     * @param row Row to spawn the NPC
     * @param col Column to spawn the NPC
     * @param speed Speed of the NPC
     * @param health Health of the NPC
     */
    public VillagerNPC(float row, float col, float speed, int health) throws
            IllegalArgumentException {
        super(row, col, speed, health);
        this.setObjectName("VillagerNPC");

        SpriteSheetAnimation animation = new SpriteSheetAnimation("Villager",
                0.1f);
        this.setAnimation(animation);

        this.setGreeting(greeting);
        this.setPeonFace("VillagerFace");
        this.setTextColor("GOLD");
    }
    @Override
    protected String getBgmPath() {
        return "Villager_Greeting" + (randomBgm.nextInt() % 2 == 0 ? "1" : "2") + ".mp3";
    }
}