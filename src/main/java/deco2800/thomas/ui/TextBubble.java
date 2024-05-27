package deco2800.thomas.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.entities.friendlyNPCs.FriendlyPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.SpriteSheetAnimation;
import deco2800.thomas.managers.TextureManager;
import com.badlogic.gdx.scenes.scene2d.Stage;


/**
 * Class to handle the TextBubble
 */
public class TextBubble {
    private Stage stage;
    private Skin skin;
    /* TextBubble Image */
    private Image background;
    /* Speaker Icon */
    private Image speaker;
    /* Dialogue Label */
    private Label dialogue;
    /* Dialogue Text */
    private String speech = "The speech has not been set";
    /* TextOption Label */
    private Label option1;
    /* Dialogue Text */
    private String sOption1 = "1: speech option 1 has not been set";
    /* TextOption Label */
    private Label option2;
    private String sOption2 = "2: speech option 2 has not been set";
    /*Text color */
    private Color color;


    /* Current player image */
    private boolean isAnimated = false;
    private SpriteSheetAnimation animation;

    /* Whether a textBubble is open */
    private boolean windowOpen = false;

    /* Game player */
    PlayerPeon player;

    public TextBubble() {
        String speech = "speech";
        stage = GameManager.get().getStage();
        skin = GameManager.get().getSkin();

        //Get textures
        background = new Image(GameManager.get()
                .getManager(TextureManager.class)
                .getTexture("textbubble"));
        speaker = new Image(GameManager.get()
                .getManager(TextureManager.class)
                .getTexture("VillagerFace"));

        background.setPosition(180, 450);
        stage.addActor(background);
        speaker.setPosition(90, 500);
        stage.addActor(speaker);

        //Text
        dialogue = new Label(speech, skin);
        dialogue.setPosition(300, 700);
        dialogue.setName(speech);
        dialogue.setFontScale(1.5f);
        dialogue.setColor(Color.NAVY);
        stage.addActor(dialogue);


        option1 = new Label(speech, skin);
        option1.setPosition(300, 670);
        option1.setName(speech);
        option1.setFontScale(1.0f);
        option1.setColor(Color.BLACK);
        stage.addActor(option1);

        option2 = new Label(speech, skin);
        option2.setPosition(300, 640);
        option2.setName(speech);
        option2.setFontScale(1.0f);
        option2.setColor(Color.BLACK);
        stage.addActor(option2);

        dispose();

    }

    public void setSpeech(String string) {
        speech = string;
    }

    public void setSpeech(String[] strings) {
        speech = strings[0];
        sOption1 = strings[1];
        sOption2 = strings[2];
    }

    public String getSpeech() {
        return speech;
    }

    /**
     * Sets the animation for the speaker icon
     * Check the animations spritesheet is registered with the TextureRegister.
     *
     * @param animation sprite sheet animation object
     */
    public void setAnimation(SpriteSheetAnimation animation) {
        this.animation = animation;
        this.isAnimated = true;
    }

    /**
     * Sets the animation for the speaker icon
     * Check the animations spritesheet is registered with the TextureRegister.
     *
     * @param npcFace sprite sheet animation object
     */
    public void setSpeaker(String npcFace) {
        speaker.setDrawable(new TextureRegionDrawable(GameManager.get()
                .getManager(TextureManager.class)
                .getTexture(npcFace)));
    }

    public void setTextColor(String color) {
        Color textColor = Color.BLACK;
        if (color.equals("NAVY")) {
            textColor = Color.NAVY;
        }
        if (color.equals("GOLD")) {
            textColor = Color.GOLD;
        }
        if (color.equals("RED")) {
            textColor = Color.RED;
        }
        dialogue.setColor(textColor);
    }



    /**
     * Changes state of window to closed (false) if opened (true). Changes state of
     * window to opened (true) if closed (false).
     */
    public void toggleWindowOpen() {
        windowOpen = !windowOpen;
        if (windowOpen) {
            create();
        } else {
            dispose();
        }
    }

    /**
     * Gets whether the TextBubble is displayed
     * @return true if opened, false if closed
     */
    public boolean getTextBubbleState() {
        return windowOpen;
    }

    /**
     * Updates the dialogue
     */
    public void update() {
        dialogue.setText(speech);
        option1.setText(sOption1);
        option2.setText(sOption2);
    }

    /**
     * Opens textbubble
     */
    public void create() {
        background.setVisible(true);
        speaker.setVisible(true);
        dialogue.setVisible(true);
        option1.setVisible(true);
        option2.setVisible(true);
    }

    /**
     * Closes textbubble
     */
    public void dispose() {
        background.setVisible(false);
        speaker.setVisible(false);
        dialogue.setVisible(false);
        option1.setVisible(false);
        option2.setVisible(false);
    }

}