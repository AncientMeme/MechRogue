package deco2800.thomas.entities;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.util.Vector2;
import com.badlogic.gdx.graphics.Color;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Random;

public class DamageIndicator implements Serializable {
    /* Amount of damage to be displayed */
    private int amount;

    /* Remaining time before this indicator is disposed */
    private float duration;

    /* Damage indicator label to be displayed on screen */
    private Label indicationLabel;

    /* Offset of the damage indicator in alpha space [0, 1] */
    private float offsetX;
    private float offsetY;

    private String displayedText;
    private Color color;
    private float scale;

    /**
     * Creates a damage indicator that will be present for 1 second
     * @param amount the amount of damage to be displayed
     */
    public DamageIndicator(int amount, Color color) {
        this.amount = amount;
        this.duration = 1f;
        Random rng = new Random();
        offsetX = rng.nextFloat();
        offsetY = rng.nextFloat();
        this.color = color;
        this.displayedText = String.format("%d", getAmount());
        this.scale = 1.0f;
    }

    /**
     * Creates a standard damage indicator that will be present for 1.5 second
     * @param text the text to be displayed
     */
    public DamageIndicator(String text, Color color) {
        this.duration = 0.5f;
        Random rng = new Random();
        offsetX = rng.nextFloat();
        offsetY = rng.nextFloat();
        this.color = color;
        this.displayedText = text;
        this.scale = 0.85f;
    }

    /**
     * Creates a label on this methods first call, else returns the
     * label that was created on the first call
     * @return this indicators label that will be added to the stage
     */
    public Label getLabel() {
        if (indicationLabel == null) {
            indicationLabel = new Label(displayedText, GameManager.get().getSkin());
            indicationLabel.setColor(color);
            indicationLabel.setFontScale(scale);
        }
        return indicationLabel;
    }

    /**
     * Returns the duration in seconds the indicator has left
     * @return the duration in seconds
     */
    public float getDuration() {
        return this.duration;
    }

    /**
     * This method determines the lifetime of all the DamageIndicators,
     * by increasing the float value, the indicators will stay on screen
     * for longer, and vice versa
     * @return
     */
    public boolean isAlive() {
        return this.duration <= 2f;
    }


    /**
     * Returns the rounded amount of damage to display
     * @return damage rounded
     */
    public int getAmount() {
        return Math.round(amount);
    }


    public Vector2 getOffset() { return new Vector2(offsetX, offsetY); }

    /**
     * Tick method to handle the duration of the indicator
     */
    public void onTick() {
        if (duration < 2f) {
            duration += 1 / 60f;
        }
    }
}
