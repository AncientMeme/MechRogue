package deco2800.thomas.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import deco2800.thomas.entities.AbstractEntity;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.buffs.Buff;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.TextureManager;


import java.util.List;

/**
 * A class to create a working health bar that applies to each individual Peon.
 */
public class BuffBar {
    SpriteBatch batch;
    Peon entity;
    float width;
    float height;
    float x;
    float y;

    /**
     * Buffbar constructor
     * @param batch batch to render ontop of
     * @param entity entity to target with buffbar
     * @param width width of entity
     * @param height height of entity
     * @param x x position of entity
     * @param y y position of entity
     */
    public BuffBar(SpriteBatch batch, Peon entity, float width, float height, float x, float y) {
        this.batch = batch;
        this.entity = entity;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    /**
     * Renders a working health bar on the screen that contains a black outline for the whole bar, a red filled in
     * rectangle that represents the max health and a green filled in rectangle that represents the current health
     */
    public void render() {
        Peon p = (Peon) entity;
        TextureRegion redPixel = GameManager.getManagerFromInstance(TextureManager.class).getTexture("red_pixel");
        TextureRegion greenPixel = GameManager.getManagerFromInstance(TextureManager.class).getTexture("green_pixel");

        List<Buff> activeBuffs = p.getBuffs();

        // Use healthbar positioning since the buffs are anchored to the hp bar
        int barWidth = 60;
        int buffIconSize = 16;
        float xCorner = x + (width-barWidth)/4;
        float yCorner = y + height + buffIconSize;

        int count = 0;
        for (Buff buff : activeBuffs) {
            TextureRegion buffTexture = GameManager.getManagerFromInstance(TextureManager.class).getTexture(buff.getTexture());
            batch.draw(buffTexture, xCorner + (buffIconSize + 2) * count, yCorner, buffIconSize, buffIconSize);
            batch.setColor(1.0f, 0.0f, 0.0f, 0.5f);
            if (buff.getIsBuff()) {
                batch.draw(greenPixel, xCorner + (buffIconSize + 2) * count, yCorner, buffIconSize, buff.getLifetimePercent() * buffIconSize);
            } else {
                batch.draw(redPixel, xCorner + (buffIconSize + 2) * count, yCorner, buffIconSize, buff.getLifetimePercent() * buffIconSize);
            }
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            count++;
        }
    }
}
