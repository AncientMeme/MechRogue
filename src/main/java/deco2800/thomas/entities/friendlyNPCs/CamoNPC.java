package deco2800.thomas.entities.friendlyNPCs;

public class CamoNPC extends FriendlyPeon{

    /*Unique greeting of Wizard*/
    String greeting = "Wanna take a ride bro?";

    /**
     * Constructor for a CamoNPC
     * @param row Row to spawn the NPC
     * @param col Column to spawn the NPC
     * @param speed Speed of the NPC
     * @param health Health of the NPC
     */
    public CamoNPC(float row, float col, float speed, int health) throws
            IllegalArgumentException {
        super(row, col, speed, health);
        this.setObjectName("CamoNPC");
        this.setTexture("camo_left"); //change texture
        this.setGreeting(greeting);
        this.setPeonFace("camo_right");
    }
}