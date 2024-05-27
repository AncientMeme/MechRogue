package deco2800.thomas.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.TextureManager;

public class GameControlsRenderer {
    private Stage stage;
    private Skin skin;
    Image background;
    Button acceptButton;

    /**
     * Constructor of the MainMenuScreen.
     */
    public GameControlsRenderer() {
        stage = (GameManager.get().getStage());
        skin = GameManager.get().getSkin();

        background = new Image(GameManager.get().getManager(TextureManager.class).getTexture("game_controls_window2"));
        background.setPosition(60, 90);
        background.setZIndex(100);
        stage.addActor(background);

        acceptButton = new TextButton("Accept", skin);
        acceptButton.setPosition(1200/2, 650/2);
        acceptButton.setZIndex(100);
        stage.addActor(acceptButton);

        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
            }
        });
    }


    /**
     * Disposes of the stage that the menu is on.
     */
    public void dispose() {
        background.setVisible(false);
        background.remove();
        acceptButton.setVisible(false);
        acceptButton.remove();
    }

}
