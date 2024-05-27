package deco2800.thomas.entities.buffs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import deco2800.thomas.entities.DamageIndicator;

public class BurnBuff extends Buff {
    /* The damage per second to the Peon */
    int dps;

    /* Make burn buff hurt the player every 60 ticks */
    int timeSinceLastDamage = 0;

    /**
     * Creates an instance of a burn buff
     * @param lifetime the longevity of the buff in seconds
     * @param dps the damage of the buff per second
     */
    public BurnBuff(float lifetime, int dps) {
        super(lifetime);
        this.dps = dps;
        this.texture = "burn_buff";
        this.isBuff = false;
    }

    @Override
    public void affectPeon()  {
        timeSinceLastDamage++;
        if (timeSinceLastDamage >= 60) {
            timeSinceLastDamage = 0;

            if (target != null) {
                target.addHealth(-dps, new DamageIndicator(-dps, Color.ORANGE));
            }
        }
    }
}
