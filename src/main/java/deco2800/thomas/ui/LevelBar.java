package deco2800.thomas.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import deco2800.thomas.entities.AbstractEntity;
import deco2800.thomas.entities.PlayerPeon;

public class LevelBar {
    AbstractEntity entity;
    SpriteBatch batch;
    float x, y;
    float width, height;
    BitmapFont font;

    /**
     * LevelBar Constructor
     * @param batch - the sprite batch
     * @param entity - the entity that this health bar is referencing
     * @param width - texture width
     * @param height - texture height
     * @param x - x coordinate of entity in the world
     * @param y - y coordinate of entity in the world
     */
    public LevelBar(SpriteBatch batch, AbstractEntity entity, float width, float height, float x, float y) {
        this.batch = batch;
        this.entity = entity;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;


        font = new BitmapFont();
        font.getData().setScale(1.3f);
    }

    /**
     * Renders a working level bar on the screen that contains a grey outline for the whole bar, a black filled in
     * rectangle that represents the max exp and a yellow filled in rectangle that represents the current exp
     */
    public void render() {
        if (entity instanceof PlayerPeon) {
            PlayerPeon p = (PlayerPeon) entity;


            // setup exp bar
            int barWidth = 160; //exp bar initial width
            int barHeight = (int)height; //exp bar initial height
            int pixHeight = 20;

            Pixmap pixmap = new Pixmap(128, 16, Pixmap.Format.RGBA8888);
            //Draw gray health bar outline
            pixmap.setColor(Color.GRAY);
            pixmap.drawRectangle(0, 0, barWidth, pixHeight);
            //Draw level bar backing
            pixmap.setColor(Color.BLACK); //set color as black
            pixmap.fillRectangle(1,1, barWidth - 2, pixHeight - 2); // draw Rectangle
            // set color as yellow

            pixmap.setColor(Color.YELLOW);

            double rate = 1.0 * p.getExp() / p.getNextLevelExp();

            pixmap.fillRectangle(1,1, (int)((barWidth - 2) * rate), pixHeight - 2);


            Texture blood = new Texture(pixmap);
            TextureRegion pix = new TextureRegion(blood, barWidth, pixHeight);
            batch.draw(pix, x-580, y-350, barWidth, pixHeight); //draw

            batch.setColor(Color.WHITE);
            pixmap.dispose();

            String s = String.format("Level:%d Exp:%d/%d", p.getLevel(), p.getExp(), p.getNextLevelExp());
            font.draw(batch, s, x-560, y-310);

        }
    }
}
