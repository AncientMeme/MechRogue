package deco2800.thomas.entities.friendlyNPCs;

public class MerchantNPC extends FriendlyPeon{

    /*Unique greeting of Wizard*/
    String greeting = "Wanna buy a watch?";

    /**
     * Constructor for a MerchantNPC
     * @param row Row to spawn the NPC
     * @param col Column to spawn the NPC
     * @param speed Speed of the NPC
     * @param health Health of the NPC
     */
    public MerchantNPC(float row, float col, float speed, int health) throws
            IllegalArgumentException {
        super(row, col, speed, health);
        this.setObjectName("MerchantNPC");
        this.setTexture("merchant_front"); //change texture
        this.setGreeting(greeting);
        this.setPeonFace("merchant_cool");
    }
    @Override
    protected String getBgmPath() {
        return "Merchant_Greeting" + (randomBgm.nextInt() % 2 == 0 ? "1" : "2") + ".mp3";
    }
}
