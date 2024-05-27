package deco2800.thomas.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import deco2800.thomas.entities.AbstractEntity;
import deco2800.thomas.entities.MechState;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.PlayerPeon;

/**
 * A class to create a working health bar that applies to each individual Peon.
 */
public class HealthBar {
    AbstractEntity entity;
    SpriteBatch batch;
    float x;
    float y;
    float width;
    float height;

    /**
     * HealthBar Constructor
     * @param batch - the sprite batch
     * @param entity - the entity that this health bar is referencing
     * @param width - texture width
     * @param height - texture height
     * @param x - x coordinate of entity in the world
     * @param y - y coordinate of entity in the world
     */
    public HealthBar(SpriteBatch batch, AbstractEntity entity, float width, float height, float x, float y) {
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
        if (entity instanceof Peon && !(entity instanceof PlayerPeon)) {
            Peon p = (Peon) entity;


            // setup health bar
            int barWidth = 60; //health bar initial width
            int barHeight = (int)height; //health bar initial height
            int barMargin = 2; // Space between bar and peon
            int pixHeight = 8;

            Pixmap pixmap = new Pixmap(128, 16, Pixmap.Format.RGBA8888);
            //Draw black health bar outline
            pixmap.setColor(Color.GRAY);
            pixmap.drawRectangle(0, 0, barWidth, pixHeight);
            //Draw health bar backing
            pixmap.setColor(Color.BLACK); //set color as red
            pixmap.fillRectangle(1,1, barWidth - 2, pixHeight - 2); // draw Rectangle
             // set color as green

            if(p.getProportionalHealth() >= 0.6f) {
                pixmap.setColor(Color.GREEN);
            } else if (p.getProportionalHealth() >= 0.3f && p.getProportionalHealth() < 0.6f) {
                pixmap.setColor(Color.ORANGE);
            } else {
                pixmap.setColor(Color.RED);
            }

            //Draw current health bar based on current health
            if (p.getProportionalHealth() == 0) {
                pixmap.fillRectangle(0,1, (int)((barWidth - 2) * p.getProportionalHealth()), pixHeight - 2);
            } else {
                pixmap.fillRectangle(1,1, (int)((barWidth - 2) * p.getProportionalHealth()), pixHeight - 2);
            }

            Texture blood = new Texture(pixmap);
            TextureRegion pix = new TextureRegion(blood, barWidth, pixHeight);
            batch.draw(pix, x + (width-barWidth)/4, y + barHeight
                    + barMargin, barWidth, pixHeight); //draw


            batch.setColor(Color.WHITE);
            pixmap.dispose();

            if(entity instanceof PlayerPeon){
                PlayerPeon pp = (PlayerPeon)entity;
                if(pp.getMechState() == MechState.MECH_ON){
                    Pixmap pixmap2 = new Pixmap(128, 16, Pixmap.Format.RGBA8888);
                    //Draw grey health bar outline
                    pixmap2.setColor(Color.GRAY);
                    pixmap2.drawRectangle(0, 0, barWidth, pixHeight);
                    //Draw health bar backing
                    pixmap2.setColor(Color.ORANGE); //set color as orange
                    pixmap2.fillRectangle(1,1, (int)((barWidth - 2) * pp.getMech().getHealth() /
                            pp.getMech().getMaxHealth()), pixHeight - 2);

                    Texture blood2 = new Texture(pixmap2);
                    TextureRegion pix2 = new TextureRegion(blood2, barWidth, pixHeight);
                    batch.draw(pix2, x + (width-barWidth)/4, y + barHeight
                            + barMargin, barWidth, pixHeight); //draw


                    batch.setColor(Color.WHITE);
                    pixmap2.dispose();
                }
            }

        }
    }
}
