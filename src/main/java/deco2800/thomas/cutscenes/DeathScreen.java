package deco2800.thomas.cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import deco2800.thomas.GameScreen;
import deco2800.thomas.ThomasGame;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.SoundManager;
import deco2800.thomas.managers.TextureManager;


public class DeathScreen implements Screen {
    private Stage stage;
    private Texture deathContainer;
    private DeathFragment deathFragment;
    private final ThomasGame game;
    private Image restart;
    private Image backMainMenu;

    public DeathScreen(final ThomasGame game) {
        this.game = game;
        this.stage = new Stage(new ExtendViewport(1280, 720), game.getBatch());
        deathContainer = new Texture(Gdx.files.internal("resources/Cut_scenes/death_cutScene.png"));
        // create death cut-scene
        deathFragment = new DeathFragment(new TextureRegion(deathContainer));
        // add death Fragment into centre of stage
        stage.addActor(deathFragment);
        deathFragment.setPosition(stage.getWidth() / 2 - deathFragment.getWidth() / 2,
                stage.getHeight() / 2 - deathFragment.getHeight() / 2);
        // restart button and back main menu button
        restart = new com.badlogic.gdx.scenes.scene2d.ui.Image(GameManager.get()
                .getManager(TextureManager.class).getTexture("restartBtn"));
        backMainMenu = new Image(GameManager.get().getManager(TextureManager.class).getTexture("mainMenu"));
        // add btns into stage
        stage.addActor(restart);
        stage.addActor(backMainMenu);
        restart.setPosition(1100, 70);
        backMainMenu.setPosition(1100,20);
        // restart function implemented
        restart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                game.setScreen(new GameScreen(GameScreen.gameType.NEW_GAME, game));
            }
        });
        backMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                game.showStartScreen();
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // set clear color as black
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update stage logic
        stage.act();
        // draw stage
        stage.draw();
    }

    public void showAnimation(){
        deathFragment.setOrigin(deathFragment.getWidth(), deathFragment.getHeight());
        deathFragment.setScale(0.5F,0.5F);
        ScaleToAction scaleToAction = Actions.scaleTo(1.0F, 1.0F, 0.6F);
        deathFragment.addAction(scaleToAction);
    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
