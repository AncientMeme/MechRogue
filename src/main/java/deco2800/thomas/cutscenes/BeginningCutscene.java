package deco2800.thomas.cutscenes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import deco2800.thomas.ThomasGame;

import java.util.ArrayList;


/**
 * A screen that shows the beginning cutScene
 * @Author Martin Gao
 */

public class BeginningCutscene implements Screen {

    // interact with the main game entity
    private final ThomasGame thomasGame;
    // textures objects that contains images as background
    private Texture imageContainer1;
    private Texture imageContainer2;
    private Texture imageContainer3;
    private Texture imageContainer4;
    private Texture imageContainer5;
    private Texture imageContainer6;
    private Texture imageContainer7;
    private Texture imageContainer8;
    private Texture imageContainer9;

    // a stage that displays all the bgs
    private Stage stage;
    // fragments that contains different pieces of bg
    private BeginningFragment fragment1;
    private BeginningFragment fragment2;
    private BeginningFragment fragment3;
    private BeginningFragment fragment4;
    private BeginningFragment fragment5;
    private BeginningFragment fragment6;
    private BeginningFragment fragment7;
    private BeginningFragment fragment8;
    private BeginningFragment fragment9;

    // the fragment position
    private int position;
    // a list that contains all fragments
    ArrayList<BeginningFragment> fragments = new ArrayList<>();
    // a time step that indicates the frequency that change a bg
    private float timeStep;
    // total time rendering
    private float timeSum;
    // texture as button up
    private Texture buttonUp;
    // texture as button down
    private Texture buttonDown;
    // button
    private Button skipButton;

    /**
     * constructor
     */
    public BeginningCutscene(ThomasGame thomasGame){
        this.thomasGame = thomasGame;
        // set an image as the bg，create as texture and add them to list
        imageContainer1 = new Texture(Gdx.files.internal("resources/Cut_scenes/scene_one.png"));
        imageContainer2 = new Texture(Gdx.files.internal("resources/Cut_scenes/scene_two.png"));
        imageContainer3 = new Texture(Gdx.files.internal("resources/Cut_scenes/scene_three.png"));
        imageContainer4 = new Texture(Gdx.files.internal("resources/Cut_scenes/scene_four.png"));
        imageContainer5 = new Texture(Gdx.files.internal("resources/Cut_scenes/scene_five.png"));
        imageContainer6 = new Texture(Gdx.files.internal("resources/Cut_scenes/scene_six.png"));
        imageContainer7 = new Texture(Gdx.files.internal("resources/Cut_scenes/scene_seven.png"));
        imageContainer8 = new Texture(Gdx.files.internal("resources/Cut_scenes/scene_eight.png"));
        imageContainer9 = new Texture(Gdx.files.internal("resources/Cut_scenes/scene_nine.png"));
        // create stage
        stage = new Stage(new ExtendViewport(1280,720));
        // create the fragments and add them to the list
        fragment1 = new BeginningFragment(new TextureRegion(imageContainer1));
        fragment2 = new BeginningFragment(new TextureRegion(imageContainer2));
        fragment3 = new BeginningFragment(new TextureRegion(imageContainer3));
        fragment4 = new BeginningFragment(new TextureRegion(imageContainer4));
        fragment5 = new BeginningFragment(new TextureRegion(imageContainer5));
        fragment6 = new BeginningFragment(new TextureRegion(imageContainer6));
        fragment7 = new BeginningFragment(new TextureRegion(imageContainer7));
        fragment8 = new BeginningFragment(new TextureRegion(imageContainer8));
        fragment9 = new BeginningFragment(new TextureRegion(imageContainer9));
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        fragments.add(fragment5);
        fragments.add(fragment6);
        fragments.add(fragment7);
        fragments.add(fragment8);
        fragments.add(fragment9);

        // set fragments to the centre of the stage
        for (BeginningFragment fragment: fragments){
            fragment.setPosition(stage.getWidth() / 2 - fragment1.getWidth() / 2,
                    stage.getHeight() / 2 - fragment1.getHeight() / 2);
        }
        // add fragment1 to the stage and into list
        stage.addActor(fragment1);
        // set input setting into stage
        Gdx.input.setInputProcessor(stage);
        // set textures as button up and down respectively
        buttonUp = new Texture(Gdx.files.internal("resources/Cut_scenes/buttonUp.png"));
        buttonDown = new Texture(Gdx.files.internal("resources/Cut_scenes/buttonDown.png"));
        // set the button style.
        Button.ButtonStyle style = new Button.ButtonStyle();
        // set button down and up texture area
        style.up = new TextureRegionDrawable(new TextureRegion(buttonUp));
        style.down = new TextureRegionDrawable(new TextureRegion(buttonDown));
        // create button
        skipButton = new Button(style);
        // set position of button
        skipButton.setPosition(575,18);
        //set listener
        skipButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                if (thomasGame != null){
                    thomasGame.showStartScreen();
                    return;
                }
            }
        });
        // add button to stage
        stage.addActor(skipButton);
    }

    @Override
    public void show() {
        timeStep = 0;
    }

    /**
     * get the current bg (cutScene fragment)
     * @return current fragment in list
     */
    public BeginningFragment getCurrentFragment(){
        return fragments.get(position);
    }

    /**
     * get the next bg (cutScene fragment)
     * @return next fragment in list
     */
    public BeginningFragment getNextFragment(){
        return fragments.get(position+1);
    }

    /**
     * draw
     * each 3s, there will be changed to next fragment, as it is end, dispose all.
     * the main menu screen will be displayed
     * @param delta render update step
     */
    @Override
    public void render(float delta) {
        timeStep += delta;
        timeSum += delta;

        if (timeStep >= 2.0F){
//            skipButton.setVisible(false);
            closeAnimation();
            if (timeStep >= 2.9F){
                stage.addActor(getNextFragment());
                getNextFragment().setVisible(true);
                startAnimation();
                position++;
                timeStep = (float) 0;
            }
        }

        if (timeSum >= fragments.size()*2.9F){
            // dispose unused resources
            imageContainer1.dispose();
            imageContainer2.dispose();
            imageContainer3.dispose();
            imageContainer4.dispose();
            imageContainer5.dispose();
            imageContainer6.dispose();
            imageContainer7.dispose();
            imageContainer8.dispose();
            imageContainer9.dispose();

            if (thomasGame != null){
                thomasGame.showStartScreen();
                return;
            }
        }

        // set clear color as black
        Gdx.gl.glClearColor(0,0,0,0);
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
        // release resources as the stage is destroyed.
        if (stage != null) {
            stage.dispose();
        } else {
            if (imageContainer1 != null){
                imageContainer1.dispose();
            }
            if (imageContainer2 != null){
                imageContainer2.dispose();
            }
            if (imageContainer3 != null){
                imageContainer3.dispose();
            }
            if (imageContainer4 != null){
                imageContainer4.dispose();
            }
            if (imageContainer5 != null){
                imageContainer5.dispose();
            }
            if (imageContainer6 != null){
                imageContainer6.dispose();
            }
            if (imageContainer7 != null){
                imageContainer7.dispose();
            }
            if (imageContainer8 != null){
                imageContainer8.dispose();
            }
            if (imageContainer9 != null){
                imageContainer9.dispose();
            }

        }
    }

    /**
     * shows close animation for each piece of cut-scene while switch to next one
     */
    public void closeAnimation(){
        // scale small
        getCurrentFragment().setScale(0.9F,0.9F);
        ScaleToAction scale = Actions.scaleTo(0.2F,0.2F, 0.5F);
        // remove into the corner
        MoveToAction move = Actions.moveTo(0,0,0.5F);
        // change into transparent
        getCurrentFragment().getColor().a = 1.0F;
        AlphaAction transparent = Actions.alpha(0.0F, 1.0F);
        // scale and move together
        ParallelAction parallelAction = Actions.parallel(scale,move);
        // scale and move first then transparent
        SequenceAction sequenceAction = Actions.sequence(parallelAction,transparent);

        getCurrentFragment().addAction(sequenceAction);

    }

    public void startAnimation(){
        // from transparent into visible
        getNextFragment().getColor().a = 0.0F;
        AlphaAction visible = Actions.alpha(1.0F, 0.5F);

        getNextFragment().addAction(visible);
    }

}
