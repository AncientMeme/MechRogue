package deco2800.thomas.entities.buffs;

import deco2800.thomas.entities.Peon;

import java.io.Serializable;

/**
 * The base class for a buff, any type of buff can be created
 * by extending from this class.
 */
public class Buff implements Serializable {
    /* The longevity the buff will last on the Peon */
    protected float lifetime;

    /* The lifetime that the buff was constructed with */
    protected float initialLifetime;

    /* The Peon that has the buff */
    protected Peon target;

    /* The buff texture */
    protected String texture;

    /* True if buff is a buff, false if buff is a debuff */
    protected boolean isBuff;

    /**
     * Creates an instance of a buff
     * @param lifetime the duration the buff will last
     */
    public Buff(float lifetime) {
        this.initialLifetime = lifetime;
        this.lifetime = lifetime;
        this.target = null;
        this.texture = "";
    }

    /**
     * Returns this buffs texture
     * @return the texture
     */
    public String getTexture() {
        return this.texture;
    }

    /**
     * Returns this buffs lifetime
     * @return current lifetime
     */
    public float getLifetime() {
        return this.lifetime;
    }

    public boolean getIsBuff() {
        return isBuff;
    }

    /**
     * Sets the lifetime of this buff
     * @param lifetime new lifetime
     */
    public void setLifetime(float lifetime) {
        this.lifetime = lifetime;
    }

    /**
     * Sets initial lifetime of this buff
     * @param initialLifetime new initial lifetime
     */
    public void setInitialLifetime(float initialLifetime) {
        this.initialLifetime = initialLifetime;
    }

    /**
     * Sets the target of the buff
     * @param target
     */
    public void setTarget(Peon target) {
        this.target = target;
    }

    /**
     * Returns ratio between initial lifetime and current lifetime.
     * Used for rendering time left
     * @return lifetime percentage
     */
    public float getLifetimePercent() {
        return this.lifetime / this.initialLifetime;
    }

    /**
     * Does this buff have any life left?
     * @return if the buff is still alive.
     */
    public boolean isAlive() {
        return this.lifetime > 0;
    }

    /**
     * Ticks the buff, does lifetime checking and
     * effect application
     */
    public void onTick() {
        if (this.lifetime > 0.0f) {
            this.lifetime -= 1/60f;
            affectPeon();
        }
    }

    /**
     * A method to be overridden in child classes of Buff.
     * This will define the behaviour the buff will have on
     * the target peon
     */
    public void affectPeon() {
        //unused in parent
    }

    /**
     * Called when the buff is being removed from the peon.
     * Gets overridden for buffs that require such an event.
     */
    public void die() {
        //unused in parent
    }
}
