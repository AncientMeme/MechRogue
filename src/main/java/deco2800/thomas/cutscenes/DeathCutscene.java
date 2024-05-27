package deco2800.thomas.cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import deco2800.thomas.ThomasGame;


public class DeathCutscene implements Screen {
    // interact with the main game entity
    private final ThomasGame thomasGame;
    // textures objects that contains images as background
    private Texture imageContainer;
    // stage that display
    private Stage stage;

    private DeathFragment deathFragment;

    public DeathCutscene(ThomasGame thomasGame){
        this.thomasGame = thomasGame;
        imageContainer = new Texture(Gdx.files.internal("resources/Cut_scenes/death_cutScene.png"));
        // create stage
        stage = new Stage(new ExtendViewport(1280,720));
        // create fragment
        deathFragment = new DeathFragment(new TextureRegion(imageContainer));
        // set fragment to the centre of stage
        deathFragment.setPosition(stage.getWidth() / 2 - deathFragment.getWidth() / 2,
                stage.getHeight() / 2 - deathFragment.getHeight() / 2);
        // add fragment to the stage
        stage.addActor(deathFragment);

    }

    @Override
    public void show() {
        //unused
    }

    @Override
    public void render(float delta) {
        // set clear color as black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update stage logic
        stage.act();
        // draw stage
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
        if (stage != null){
            stage.dispose();
            stage = null;
        }else {
            if (imageContainer != null){
                imageContainer.dispose();
                imageContainer = null;
            }
        }
    }
}
