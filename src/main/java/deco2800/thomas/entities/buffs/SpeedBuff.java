package deco2800.thomas.entities.buffs;

public class SpeedBuff extends Buff {
    /* Percentage speed increase from buff */
    private float percentageIncrease;

    /* The actual peons speed before applying speed */
    private float actualSpeed;

    public SpeedBuff(float lifetime, float percentageIncrease) {
        super(lifetime);
        this.percentageIncrease = percentageIncrease;
        this.texture = "speed_buff";
        this.isBuff = true;
    }

    public void setActualPeonSpeed(float speed) {
        actualSpeed = speed;
    }

    // Slow player down
    @Override
    public void affectPeon() {
        if (target != null) {
            target.setSpeedByBuff(actualSpeed * (1.0f + percentageIncrease));
        }
    }

    @Override
    public void die() {
        if (target != null) {
            target.setSpeed(actualSpeed);
        }
    }
}
