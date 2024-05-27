package deco2800.thomas.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import deco2800.thomas.entities.MechState;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.TextureManager;

public class StatusBarRenderer {
    private Stage stage;
    private Skin skin;
    private PlayerPeon player;

    /* UI Position */
    private int x = 40;
    private int y = 60;
    private int pixelScale = 3;


    /* Image resources used for the status bar */
    private Image statusBorder;
    private Image healthBar;
    private Image expBar;
    private Image hurtBar;
    private Image mechHealthBar;

    /* Additional components for the status bar */
    private Label level;
    private Label health;
    private float maxHealthSize;
    private float maxExpSize;

    /**
     * Initialize a status bar that displays player health, experience, and
     * level
     * */
    public StatusBarRenderer(PlayerPeon player) {
        // Get the stage and skin from game manager
        stage = GameManager.get().getStage();
        skin = GameManager.get().getSkin();

        // Get the player peon
        this.player = player;

        // Get the sprites for the Status bar
        statusBorder = new Image(GameManager.get().
                getManager(TextureManager.class).
                getTexture("status_bar"));

        statusBorder.setPosition(120, 120);
        healthBar = new Image(GameManager.get().
                getManager(TextureManager.class).
                getTexture("player_health_bar"));
        maxHealthSize = healthBar.getWidth();

        hurtBar = new Image(GameManager.get().
                getManager(TextureManager.class).
                getTexture("player_hurt_bar"));

        expBar = new Image(GameManager.get().
                getManager(TextureManager.class).
                getTexture("player_exp_bar"));
        maxExpSize = expBar.getWidth();

        statusBorder.setPosition(x, y);
        stage.addActor(statusBorder);

        hurtBar.setPosition(x + 32 * pixelScale, y + 7 * pixelScale);
        stage.addActor(hurtBar);

        healthBar.setPosition(x + 32 * pixelScale, y + 7 * pixelScale);
        stage.addActor(healthBar);

        expBar.setPosition(x + 48 * pixelScale, y + 20 * pixelScale);
        stage.addActor(expBar);

        // Add the mech health value indicator
        mechHealthBar = new Image(GameManager.get().
                getManager(TextureManager.class).
                getTexture("mech_health_bar"));
        mechHealthBar.setPosition(x + 32 * pixelScale, y + 7 * pixelScale);
        stage.addActor(mechHealthBar);

        // Add the level indicator
        level = new Label(String.format("%d", player.getLevel()), skin);
        level.setPosition(x + 36 * pixelScale, y + 19 * pixelScale);
        level.setName("level");
        level.setFontScale(1.0f);
        stage.addActor(level);

        // Add the health value indicator
        health = new Label(String.format("%d", player.getHealth()), skin);
        health.setPosition(x + 32 * pixelScale, y + 8 * pixelScale);
        health.setName("health");
        health.setFontScale(1.0f);
        stage.addActor(health);
    }

    public void update() {
        // Change the health bar size
        float healthPercent = (float)player.getProportionalHealth();
        float healthWidth = maxHealthSize * healthPercent;
        healthBar.setWidth(healthWidth);

        // Resize the damage indicator
        resizeHurtBar();

        // Change the exp bar size
        float expPercent = (float)player.getExp()/player.getNextLevelExp();
        float expWidth = maxExpSize * expPercent;
        expBar.setWidth(expWidth);

        // Update player's current health and level text
        level.setText(player.getLevel());
        health.setText(player.getHealth());

        if(player.getMechState() == MechState.MECH_ON){
            healthPercent = (float)(Math.round(player.getMech().getHealth() * 100.0 / player.getMech().getMaxHealth()) / 100.0);
            healthWidth = maxHealthSize * healthPercent;
            mechHealthBar.setWidth(healthWidth);

            mechHealthBar.setVisible(true);
            health.setText(player.getMech().getHealth());
        }else{
            mechHealthBar.setVisible(false);
        }
    }

    public void resizeHurtBar() {
        float distance = hurtBar.getWidth() - healthBar.getWidth();
        float width = hurtBar.getWidth() - (float)(distance * 0.1);
        if(Math.abs(distance) < 3){
            width = healthBar.getWidth();
        }
        hurtBar.setWidth(width);
    }

}
