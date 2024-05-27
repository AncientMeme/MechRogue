package deco2800.thomas.setting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import deco2800.thomas.GameScreen;
import deco2800.thomas.ThomasGame;
import deco2800.thomas.mainmenu.MainMenuScreen;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.SoundManager;
import deco2800.thomas.managers.TextureManager;


public class Setting implements Screen {
    private Texture logoTexture;

    private Stage stage;

    public Setting(final ThomasGame game) {

        stage = new Stage(new ExtendViewport(1280, 720));
        Texture texture = new Texture(Gdx.files.internal("resources/used_images/UI/Settingscreen.png"));
        Image background = new Image(texture);
        background.setFillParent(true);
        stage.addActor(background);

        Texture checked = new Texture(Gdx.files.internal("resources/used_images/UI/Backbutton.png"));
        Texture imgUp = new Texture(Gdx.files.internal("resources/used_images/UI/Backbutton.png"));
        TextureRegion region = new TextureRegion(checked);
        TextureRegion regionDown = new TextureRegion(imgUp);
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(regionDown), new TextureRegionDrawable(region));
        imageButton.setSize(75, 75);
        imageButton.setPosition(30, 15);
        stage.addActor(imageButton);


        imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SoundManager.linsterMusic();
                game.setScreen(new MainMenuScreen(game));
            }
        });

    }
    @Override
    public void show() {
        //unused
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //unused
    }

    @Override
    public void pause() {
        //unused
    }

    @Override
    public void resume() {
        //unused
    }

    @Override
    public void hide() {
        //unused
    }

    @Override
    public void dispose() {

        if (stage != null) {
            stage.dispose();
        }

    }
}
