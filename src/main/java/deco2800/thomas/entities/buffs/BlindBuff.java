package deco2800.thomas.entities.buffs;

/**
 * As a child of the Buff class, this class implements specific
 * functionality to blind any peon. Blind will disable the peon
 * from using melee attacks for its duration
 */
public class BlindBuff extends Buff {
    /**
     * Creates an instance of a blind buff
     * @param lifetime the longevity the buff will last
     */
    public BlindBuff(float lifetime) {
        super(lifetime);
        this.texture = "blind_buff";
        this.isBuff = false;
    }
}
