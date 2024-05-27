package deco2800.thomas.entities.buffs;

import com.badlogic.gdx.graphics.Color;
import deco2800.thomas.entities.DamageIndicator;

import javax.swing.plaf.ColorUIResource;

public class RegenBuff extends Buff {
    /* Health per second gained from buff */
    private int hps;

    /* Make regen buff heal the player every 60 ticks */
    int timeSinceLastDamage = 0;

    public RegenBuff(float lifetime, int hps) {
        super(lifetime);
        this.hps = hps;
        this.isBuff = true;
        this.texture = "regen_buff";
        target.addIndicator(new DamageIndicator("REGENERATING", Color.GREEN));
    }

    @Override
    public void affectPeon()  {
        timeSinceLastDamage++;
        if (timeSinceLastDamage >= 60) {
            timeSinceLastDamage = 0;

            if (target != null) {
                target.addHealth(hps);
                target.addIndicator(new DamageIndicator("+ " + hps, Color.GREEN));
            }
        }
    }
}
