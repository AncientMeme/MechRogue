package deco2800.thomas.entities.buffs;

import com.badlogic.gdx.graphics.Color;
import deco2800.thomas.entities.DamageIndicator;

public class PoisonBuff extends Buff {
    /* The damage per second to the Peon */
    private int dps;

    /* Make poison buff hurt the player every 60 ticks */
    int timeSinceLastDamage = 0;

    /**
     * Creates an instance of a burn buff
     * @param lifetime the longevity of the buff in seconds
     * @param dps the damage of the buff per second
     */
    public PoisonBuff(float lifetime, int dps) {
        super(lifetime);
        this.dps = dps;
        this.isBuff = false;
        this.texture = "poison_buff";
    }

    @Override
    public void affectPeon()  {
        timeSinceLastDamage++;
        if (timeSinceLastDamage >= 60) {
            timeSinceLastDamage = 0;

            if (target != null) {
                target.addHealth(-dps, new DamageIndicator(-dps, Color.FOREST));
            }
        }
    }
}
