package deco2800.thomas.entities.friendlyNPCs;

import deco2800.thomas.managers.SpriteSheetAnimation;
public class WizardNPC extends FriendlyPeon{

    /*Unique greeting of Wizard*/
    String greeting = "A wizard is never late!";

    /**
     * Constructor for a WizardNPC
     * @param row Row to spawn the NPC
     * @param col Column to spawn the NPC
     * @param speed Speed of the NPC
     * @param health Health of the NPC
     */
    public WizardNPC(float row, float col, float speed, int health) throws
            IllegalArgumentException {
        super(row, col, speed, health);
        this.setObjectName("WizardNPC");

        SpriteSheetAnimation animation = new SpriteSheetAnimation("WizardNpc1",
                0.1f);
        this.setAnimation(animation);

        this.setGreeting(greeting);

        this.setPeonFace("WizardFace");
        this.setTextColor("NAVY");
        this.setDialoguePath("resources/dialogue_trees/Wizard.txt");
    }
    @Override
    protected String getBgmPath() {
        return "Wizard_Greeting" + (randomBgm.nextInt() % 2 == 0 ? "1" : "2") + ".mp3";
        }
}
