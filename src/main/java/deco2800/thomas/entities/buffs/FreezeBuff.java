package deco2800.thomas.entities.buffs;

import com.badlogic.gdx.graphics.Color;
import deco2800.thomas.entities.DamageIndicator;

public class FreezeBuff extends Buff {
    /* The amount to reduce the Peons movement speed by */
    private float percentDecrease;

    /* The actual peons speed before applying slowness */
    private float actualSpeed;

    /**
     * Creates an instance of a freeze buff
     * @param lifetime the longevity of the buff in seconds
     * @param percentDecrease the percent to decrease the players speed [0 - 1]
     */
    public FreezeBuff(float lifetime, float percentDecrease) {
        super(lifetime);
        this.percentDecrease = percentDecrease;
        this.isBuff = false;
        this.texture = "freeze_buff";
    }

    public void setActualPeonSpeed(float speed) {
        actualSpeed = speed;
    }

    // Slow player down
    @Override
    public void affectPeon() {
        if (target != null) {
            target.setSpeedByBuff(actualSpeed * (1.0f - percentDecrease));
        }
    }

    @Override
    public void die() {
        if (target != null) {
            target.setSpeed(actualSpeed);
        }
    }
}
