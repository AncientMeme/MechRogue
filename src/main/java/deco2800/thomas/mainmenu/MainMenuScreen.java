package deco2800.thomas.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import deco2800.thomas.GameScreen;
import deco2800.thomas.ThomasGame;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.SoundManager;
import deco2800.thomas.managers.TextureManager;
import deco2800.thomas.setting.Setting;

public class MainMenuScreen implements Screen {
    final ThomasGame game;
    private Stage stage;


    /**
     * Constructor of the MainMenuScreen.
     * @param game the game to run
     */
    public MainMenuScreen(final ThomasGame game) {
        this.game = game;

        stage = new Stage(new ExtendViewport(1280, 720), game.getBatch());

        Image background = new Image(GameManager.get().getManager(TextureManager.class).getTexture("background"));
        background.setFillParent(true);
        stage.addActor(background);

        Texture checked=new Texture(Gdx.files.internal("resources/used_images/UI/SINGLEPLAYER-1.png"));
        Texture imgUp=new Texture(Gdx.files.internal("resources/used_images/UI/SINGLEPLAYER.png"));
        TextureRegion region=new TextureRegion(checked);
        TextureRegion regionDown = new TextureRegion(imgUp);
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(regionDown), new TextureRegionDrawable(region));
        imageButton.setSize(200,100);
        //imageButton.sizeBy(30,120);
        imageButton.setPosition(30,400);
        stage.addActor(imageButton);

        Texture checked1=new Texture(Gdx.files.internal("resources/used_images/UI/LOADGAME-1.png"));
        Texture imgUp1=new Texture(Gdx.files.internal("resources/used_images/UI/LOADGAME.png"));
        TextureRegion region1=new TextureRegion(checked1);
        TextureRegion regionDown1 = new TextureRegion(imgUp1);
        ImageButton loadGameButton  = new ImageButton(new TextureRegionDrawable(regionDown1), new TextureRegionDrawable(region1));
        loadGameButton.setSize(200,100);
        //loadGameButton.sizeBy(30,60);
        //loadGameButton.setScale(30,60);
        loadGameButton.setPosition(30, 300);
        stage.addActor(loadGameButton);

        Texture checked3=new Texture(Gdx.files.internal("resources/used_images/UI/SETTING-1.png"));
        Texture imgUp3=new Texture(Gdx.files.internal("resources/used_images/UI/SETTING.png"));
        TextureRegion region3=new TextureRegion(checked3);
        TextureRegion regionDown3 = new TextureRegion(imgUp3);
        ImageButton settingButton  = new ImageButton(new TextureRegionDrawable(regionDown3), new TextureRegionDrawable(region3));
        settingButton.setSize(200,100);
        //settingButton.sizeBy(30,0);
       // settingButton.setScale(30,0);
        settingButton.setPosition(30, 200);
        stage.addActor(settingButton);

        Texture checked2=new Texture(Gdx.files.internal("resources/used_images/UI/CTS-1.png"));
        Texture imgUp2=new Texture(Gdx.files.internal("resources/used_images/UI/CTS.png"));
        TextureRegion region2=new TextureRegion(checked2);
        TextureRegion regionDown2 = new TextureRegion(imgUp2);
        ImageButton connectToServerButton  = new ImageButton(new TextureRegionDrawable(regionDown2), new TextureRegionDrawable(region2));
        connectToServerButton.setSize(200,100);
       // connectToServerButton.sizeBy(200,400);
        //connectToServerButton.setScale(30,-60);
        connectToServerButton.setPosition(30, 100);
        stage.addActor(connectToServerButton);


        connectToServerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.linsterMusic();
                game.setScreen(new GameScreen(GameScreen.gameType.CONNECT_TO_SERVER, game));
            }
        });

        settingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.linsterMusic();
                game.setScreen(new Setting(game));
            }
        });

        loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.linsterMusic();
                game.setScreen(new GameScreen(GameScreen.gameType.LOAD_GAME, game));
            }
        });

        imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.linsterMusic();
                game.setScreen(new GameScreen(GameScreen.gameType.NEW_GAME, game));
            }
        });
        SoundManager.get().playMusic("MainMenuMusic.mp3");
    }

    
   /**
     * Begins things that need to begin when shown.
     */
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Pauses the screen.
     */
    public void pause() {
        //do nothing
    }

    /**
     * Resumes the screen.
     */
    public void resume() {
        //do nothing
    }

    /**
     * Hides the screen.
     */
    public void hide() {
        //do nothing
    }

    /**
     * Resizes the main menu stage to a new width and height.
     * @param width the new width for the menu stage
     * @param height the new width for the menu stage
     */
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Renders the menu.
     * @param delta
     */
    public void render (float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    /**
     * Disposes of the stage that the menu is on.
     */
    public void dispose() {
        stage.dispose();
    }
}
