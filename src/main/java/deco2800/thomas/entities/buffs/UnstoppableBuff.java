package deco2800.thomas.entities.buffs;

public class UnstoppableBuff extends Buff {
    private int hps;
    private float percentageIncrease;

    public UnstoppableBuff(float lifetime, int hps, float percentageIncrease) {
        super(lifetime);
        this.hps = hps;
        this.percentageIncrease = percentageIncrease;
        this.texture = "unstoppable_buff";
        this.isBuff = true;
    }

    public float getPercentageIncrease() {
        return percentageIncrease;
    }

    public int getHps() {
        return hps;
    }
}
