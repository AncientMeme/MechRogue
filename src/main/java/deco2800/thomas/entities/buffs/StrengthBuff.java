package deco2800.thomas.entities.buffs;

public class StrengthBuff extends Buff {
    /* Added attack damage */
    private float percentageIncrease;

    public StrengthBuff(float lifetime, float percentageIncrease) {
        super(lifetime);
        this.percentageIncrease = percentageIncrease;
        this.texture = "strength_buff";
        this.isBuff = true;
    }

    public float getStrength() {
        return percentageIncrease;
    }
}
