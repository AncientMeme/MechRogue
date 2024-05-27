package deco2800.thomas.entities.friendlyNPCs;

public class SnowmanNPC extends FriendlyPeon{

    /*Unique greeting of Wizard*/
    String greeting = "Man it's freezing here right?";

    /**
     * Constructor for a SnowmanNPC
     * @param row Row to spawn the NPC
     * @param col Column to spawn the NPC
     * @param speed Speed of the NPC
     * @param health Health of the NPC
     */
    public SnowmanNPC(float row, float col, float speed, int health) throws
            IllegalArgumentException {
        super(row, col, speed, health);
        this.setObjectName("SnowmanNPC");
        this.setTexture("snowman_left"); //change texture
        this.setGreeting(greeting);
        this.setPeonFace("snowman_right");
    }
}