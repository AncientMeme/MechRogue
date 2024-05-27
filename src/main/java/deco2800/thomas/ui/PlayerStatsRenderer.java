package deco2800.thomas.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import deco2800.thomas.entities.MechState;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.TextureManager;
import com.badlogic.gdx.scenes.scene2d.Stage;


/**
 * Class to handle rendering the player stats window
 */
public class PlayerStatsRenderer {
    private Stage stage;
    private Skin skin;
    /* All the buttons in the stat window */
    Button[] buttons;
    /* All the labels in the stat window */
    Label[] labels;
    /* The background of the stat window */
    Image background;
    /* Current player image */
    Image playerIcon;
    Image mechPlayerIcon;

    /* Whether stats window is currently open */
    private boolean windowOpen = false;

    /* Game player */
    PlayerPeon player;

    /* Layout Constants */
    private static final int BUTTONS_X = 1188;
    private static final int LABELS_X = 1092;

    public PlayerStatsRenderer(PlayerPeon player) {
        this.player = player;

        stage = GameManager.get().getStage();
        skin = GameManager.get().getSkin();

        //Get textures
        background = new Image(GameManager.get()
                .getManager(TextureManager.class)
                .getTexture("stats_window"));
        playerIcon = new Image(GameManager.get()
                .getManager(TextureManager.class)
                .getTexture("player_idle_down_still"));
        mechPlayerIcon = new Image(GameManager.get()
                .getManager(TextureManager.class)
                .getTexture("Mech_001"));

        background.setPosition(690, 220);
        stage.addActor(background);

        playerIcon.setScale(0.3f);
        playerIcon.setPosition(1030, 635);
        stage.addActor(playerIcon);

        mechPlayerIcon.setScale(0.4f);
        mechPlayerIcon.setPosition(1030, 635);
        stage.addActor(mechPlayerIcon);
        mechPlayerIcon.setVisible(false);


        //Text displaying current player level
        Label level = new Label(String.format("%d", player.getLevel()), skin);
        level.setPosition(1005, 607);
        level.setName("level");

        //Text displaying current player EXP
        Label exp = new Label(String.format("%d / %d", player.getExp(), player.getNextLevelExp()), skin);
        exp.setPosition(980, 587);
        exp.setName("exp");

        //Text displaying players remaining stats
        Label statPoints = new Label(String.format("%d", player.getStatPoints()), skin);
        statPoints.setPosition(1080, 567);
        statPoints.setName("statPoints");

        //Text displaying players current strength
        Label strength = new Label(String.format("%d", player.getStrength()), skin);
        strength.setPosition(LABELS_X, 512);

        //Text displaying players current defense
        Label defense = new Label(String.format("%d%%", player.getDefense()), skin);
        defense.setPosition(LABELS_X, 457);

        //Text displaying players current speed
        Label speed = new Label(String.format("%.2f", player.getSpeed()), skin);
        speed.setPosition(LABELS_X, 396);

        //Text displaying players maximum health
        Label maxHealth = new Label(String.format("%d", player.getMaxHealth()), skin);
        maxHealth.setPosition(LABELS_X, 338);

        labels = new Label[]{level, statPoints, strength, defense, speed, maxHealth, exp};

        for (Label label : labels) {
            label.setFontScale(1.0f);
            stage.addActor(label);
        }

        //Create buttons
        Button strengthButton = new TextButton(" + ", skin);
        strengthButton.setPosition(BUTTONS_X, 510);
        Button defenseButton = new TextButton(" + ", skin );
        defenseButton.setPosition(BUTTONS_X, 455);
        Button speedButton = new TextButton(" + ", skin );
        speedButton.setPosition(BUTTONS_X, 394);
        Button maxHealthButton = new TextButton(" + ", skin);
        maxHealthButton.setPosition(BUTTONS_X, 336);

        buttons = new Button[]{strengthButton, defenseButton, speedButton, maxHealthButton};

        for (Button button : buttons) {
            stage.addActor(button);
        }

        //Event handler for strength button
        strengthButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player.getStatPoints() > 0) {
                    player.setStrength(player.getStrength() + player.getLevel());
                    player.setStatPoints(player.getStatPoints() - 1);
                    strength.setText(String.format("%d", player.getStrength()));
                }
            }
        });

        //Event handler for defense button
        defenseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player.getStatPoints() > 0) {
                    if (player.getDefense() < 50) {
                        player.setDefense(player.getDefense() + 10);
                        player.setStatPoints(player.getStatPoints() - 1);
                    }
                    defense.setText(String.format("%d", player.getDefense()));
                }
            }
        });

        //Event handler for speed button
        speedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player.getStatPoints() > 0) {
                    player.setSpeed(player.getSpeed() + 0.01f);
                    player.setStatPoints(player.getStatPoints() - 1);
                    speed.setText(String.format("%.2f", player.getSpeed()));
                }
            }
        });

        //Event handler for max health button
        maxHealthButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player.getStatPoints() > 0) {
                    player.setMaxHealth(player.getMaxHealth() + 15);
                    player.setStatPoints(player.getStatPoints() - 1);
                    maxHealth.setText(String.format("%d", player.getMaxHealth()));
                }
            }
        });
        //Stats window closed when game starts
        dispose();
    }

    /**
     * Changes state of window to closed (false) if opened (true). Changes state of
     * window to opened (true) if closed (false).
     */
    public void toggleWindowOpen() {
        windowOpen = windowOpen ? false : true;
        if (windowOpen) {
            create();
        } else {
            dispose();
        }
    }

    /**
     * Gets whether the stats window is open
     * @return true if opened, false if closed
     */
    public boolean getStatsWindowState() {
        return windowOpen;
    }

    /**
     * Updates the level and statpoint labels. If player is in the mech, displays added stats provided by the mech
     */
    public void update() {
        for (Label label : labels) {
            if (label.getName() == "level") {
                label.setText(player.getLevel());
            } else if (label.getName() == "statPoints") {
                label.setText(player.getStatPoints());
            } else if (label.getName() == "exp") {
                label.setText(String.format("%d / %d", player.getExp(), player.getNextLevelExp()));
            }
        }

        if (windowOpen) {
            if (player.getMechState() == MechState.MECH_ON) {
                mechPlayerIcon.setVisible(true);
                playerIcon.setVisible(false);

            } else {
                playerIcon.setVisible(true);
                mechPlayerIcon.setVisible(false);

            }

            if (player.getMechState() == MechState.MECH_ON) {
                labels[2].setText(String.format("%d       (+%d)", player.getStrength(),
                        player.getMech().getStrength()));
                labels[3].setText(String.format("%d       (+%d)", player.getDefense(),
                        player.getMech().getDefense()));
                labels[4].setText(String.format("%.2f  (-%.2f)", player.getSpeed() + player.getMech().getSpeedDeduction(),
                        player.getMech().getSpeedDeduction()));
                labels[5].setText(String.format("%d     (+%d)", player.getMaxHealth(),
                        player.getMech().getHealth()));
            }else {
                labels[2].setText(String.format("%d", player.getStrength()));
                labels[3].setText(String.format("%d", player.getDefense()));
                labels[4].setText(String.format("%.2f", player.getSpeed()));
                labels[5].setText(String.format("%d", player.getMaxHealth()));
            }
        }
    }

    /**
     * Opens stat window/ makes actors active and visible.
     */
    public void create() {
        for (Label label : labels) {
            label.setVisible(true);
        }
        for (Button button : buttons) {
            button.setVisible(true);

        }
        background.setVisible(true);
    }

    /**
     * Closes stat window/ makes actors inactive and invisible
     */
    public void dispose() {
        for (Label label : labels) {
            label.setVisible(false);
        }
        for (Button button : buttons) {
            button.setVisible(false);
        }
        background.setVisible(false);
        playerIcon.setVisible(false);
        mechPlayerIcon.setVisible(false);
    }
}
