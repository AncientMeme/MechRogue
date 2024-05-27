package deco2800.thomas.entities.friendlyNPCs;

public class Wizard2NPC extends FriendlyPeon{

    String greeting = "Ava'cavado!";

    /**Constructor*/
    public Wizard2NPC(float row, float col, float speed, int health) throws
            IllegalArgumentException {
        super(row, col, speed, health);
        this.setObjectName("Wizard2NPC");
        this.setTexture("wizard_right"); //change texture
        this.setGreeting(greeting);
        this.setPeonFace("wizard_front");
        this.setTextColor("NAVY");
    }
    @Override
    protected String getBgmPath() {
        return "Wizard2_Greeting" + (randomBgm.nextInt() % 2 == 0 ? "1" : "2") + ".mp3";
    }
}
