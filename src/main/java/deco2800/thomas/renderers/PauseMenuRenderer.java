package deco2800.thomas.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import deco2800.thomas.GameScreen;
import deco2800.thomas.ThomasGame;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.SoundManager;
import deco2800.thomas.tasks.PauseGameTask;

/**
 * Renders the PauseMenu when the game is paused. Is also responsible for handling the buttons
 * on the pause menu. (Load, Save, Resume, Quit).
 *
 * Class uses singleton implementation so as not to get copied when game is reloaded.
 */
public class PauseMenuRenderer implements Renderer {
    private static boolean paused = false;
    private static Stage stage;
    private static ImageButton resumeButton, loadGameButton, saveGameButton, quitGameButton, square;
    private static GameScreen gameScreen;
    private static ThomasGame game;
    private static PauseMenuRenderer instance = null;
    private static String musicName;

    /**
     * Private constructor
     * @param stage stage parameter used to draw the menu
     * @param gameScreen used to load and quit the game
     * @param game used to quit the game
     */
    private PauseMenuRenderer(Stage stage, GameScreen gameScreen, ThomasGame game){
        paused = false;
        this.stage = stage;
        this.gameScreen = gameScreen;
        this.game = game;
        instance = this;
    }

    /**
     * Singleton constructor
     * @param stage stage parameter used to draw the menu
     * @param gameScreen used to load and quit the game
     * @param game used to quit the game
     * @return New instance if non-existent. Else existing instance.
     */
    public static PauseMenuRenderer get(Stage stage, GameScreen gameScreen, ThomasGame game){
        if(instance == null){
            return new PauseMenuRenderer(stage, gameScreen, game);
        }
        return instance;
    }

    @Override
    public void render(SpriteBatch batc, OrthographicCamera camera) {
        //unused
    }

    /**
     * Used by tickables to check if the game is paused
     * @return true if paused.
     */
    public static boolean isPaused(){
        return paused;
    }

    /**
     * Called by PauseGameTask to pause the game.
     * Called by resume button to unpause.
     */
    public static void togglePause(){
        if(paused){
            unPause();
        }
        else{
            pause();
        }
    }

    /**
     * Unpauses the game
     */
    private static void unPause() {
        paused = false;
        derenderPauseMenu();
    }

    /**
     * Removes the pause menu elements from the stage.
     */
    private static void derenderPauseMenu(){
        for(Actor actor : stage.getActors()) {
            //actor.remove();
            if(actor.equals(resumeButton)||actor.equals(loadGameButton)||actor.equals(saveGameButton)||actor.equals(quitGameButton)||actor.equals(square)){
                actor.addAction(Actions.removeActor());
            }
        }
    }

    /**
     * Pauses the game
     */
    private static void pause(){
        paused = true;
        renderPauseMenu();
    }

    /**
     * Adds the pause menu elements to the stage
     */
    private static void renderPauseMenu(){
        Texture checked0=new Texture(Gdx.files.internal("resources/UI/pause_menu_background2.png"));
        TextureRegion region0 = new TextureRegion(checked0);
        square = new ImageButton(new TextureRegionDrawable(region0), new TextureRegionDrawable(region0));

        square.setPosition(0, 0);
        square.setColor(1, 1, 1, (float)0.5);
        stage.addActor(square);

        Texture checked=new Texture(Gdx.files.internal("resources/used_images/UI/resume-2.png"));
        Texture imgUp=new Texture(Gdx.files.internal("resources/used_images/UI/resume-1.png"));
        TextureRegion region=new TextureRegion(checked);
        TextureRegion regionDown = new TextureRegion(imgUp);
        resumeButton = new ImageButton(new TextureRegionDrawable(regionDown), new TextureRegionDrawable(region));
        resumeButton.setSize(200,100);
        resumeButton.setPosition(525,600);
        stage.addActor(resumeButton);

        Texture checked1=new Texture(Gdx.files.internal("resources/used_images/UI/LOADGAME-1.png"));
        Texture imgUp1=new Texture(Gdx.files.internal("resources/used_images/UI/LOADGAME.png"));
        TextureRegion region1=new TextureRegion(checked1);
        TextureRegion regionDown1 = new TextureRegion(imgUp1);
        loadGameButton  = new ImageButton(new TextureRegionDrawable(regionDown1), new TextureRegionDrawable(region1));
        loadGameButton.setSize(200,100);
        loadGameButton.setPosition(525, 500);
        stage.addActor(loadGameButton);

        Texture checked3=new Texture(Gdx.files.internal("resources/used_images/UI/savegame-2.png"));
        Texture imgUp3=new Texture(Gdx.files.internal("resources/used_images/UI/savegame-1.png"));
        TextureRegion region3=new TextureRegion(checked3);
        TextureRegion regionDown3 = new TextureRegion(imgUp3);
        saveGameButton = new ImageButton(new TextureRegionDrawable(regionDown3), new TextureRegionDrawable(region3));
        saveGameButton.setSize(200,100);
        saveGameButton.setPosition(525, 400);
        stage.addActor(saveGameButton);

        Texture checked2=new Texture(Gdx.files.internal("resources/used_images/UI/quit-2.png"));
        Texture imgUp2=new Texture(Gdx.files.internal("resources/used_images/UI/quit-1.png"));
        TextureRegion region2=new TextureRegion(checked2);
        TextureRegion regionDown2 = new TextureRegion(imgUp2);
        quitGameButton = new ImageButton(new TextureRegionDrawable(regionDown2), new TextureRegionDrawable(region2));
        quitGameButton.setSize(200,100);
        quitGameButton.setPosition(525, 300);
        stage.addActor(quitGameButton);


        quitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.linsterMusic();
                gameScreen.dispose();
            }
        });

        saveGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.linsterMusic();
                try{
                    GameManager.get().getWorld().serialize("save.txt");

                } catch(Exception e){};
            }
        });

        loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.linsterMusic();
                game.setScreen(new GameScreen(GameScreen.gameType.LOAD_GAME, game));
                togglePause();
            }
        });

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.linsterMusic();
                togglePause();
            }
        });
    }


}
