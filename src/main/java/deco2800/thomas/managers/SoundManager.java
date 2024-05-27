package deco2800.thomas.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import deco2800.thomas.observers.TileEventObservable;
import deco2800.thomas.observers.TileEventObserver;

import java.io.Serializable;
import java.lang.Math;

public class SoundManager extends TickableManager implements TileEventObserver, Serializable {

    private transient static SoundManager instance = null;
    private transient Music music = null;

    /**Music State*/
    String currentBiome;
    String currentState = "STATE_EJECTED";
    String previousState = "STATE_EJECTED";
    //validStates = ["STATE_EJECTED"] TODO: Have a list of states that setState() can check against
    float volume = 1.0f;
    float maxVolume = 1.0f;
    float delta = 0.001f;
    float fadeTime = 1.0f;

    /**Music Filenames*/
    String tundraST = "TundraEnvMusic.mp3";
    String swampST = "SwampEnvMusic.mp3";
    String desertST = "desertEnvMusic.mp3";
    String volcanicST = "VolcanicEnvMusic.mp3";

	
    public void playSound(String soundName) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("resources/sounds/" + soundName));
        sound.play(1);
    }

    /**
     * Plays music
     *
     */
    public void playMusic(String musicName) {
        if (music != null) {
            music.dispose();
        }
        music = Gdx.audio.newMusic(Gdx.files.internal("resources/sounds/" + musicName));
        music.play();
    }


    public void playGameMusic(String musicName) {
        if (music != null) {
            music.dispose();
        }
        music = Gdx.audio.newMusic(Gdx.files.internal("resources/sounds/" + musicName));
        setState("STATE_PLAYING");
        music.setVolume(0.0f);
        this.volume = 0.0f;
        music.play();
        setState("STATE_FADEIN");
    }

    public static void linsterMusic() {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("resources/sounds/StonePushing.mp3"));
        sound.play(1);
    }

    /**
     * Set volume between 1 and 0
     * @param volume
     */
    public void setMaxVolume(float volume) {
        if (volume <= 1 && volume > 0) {
            this.maxVolume = volume;
        }
    }


    public void setState(String state) { //TODO: Method to set the state of the music
        previousState = currentState;
        currentState = state;
        System.out.println("This ran");
    }

    public void setBiome(String biome) { //TODO: Method to set the state of the music
        currentBiome = biome;
    }

    /**
     * Returns an instance of the SM
     *
     * @return SoundManager
     */
    public static SoundManager get() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    //TODO: if Soundmanger implements BiomeventObserver (after tile type is implemented)
//    /**
//     * Called when the player enters the boundaries of the tile
//     *
//     * @param biome  biome type that the event triggers on.
//     * @param player reference to player
//     * @param event
//     */
//    @Override
//    public void notifyEvent(String biome, TileEventObservable player, TileEventObserver event) {
//
//        if (biome == "swamp" && currentBiome != "swamp"){
//            playMusic(swampST);
//            setBiome("swamp");
//        }
//
//        if (biome == "desert" && currentBiome != "desert"){
//            playMusic(desertST);
//            setBiome("desert");
//        }
//
//        if (biome == "volcanic" && currentBiome != "volcanic"){
//            playMusic(volcanicST);
//            setBiome("volcanic");
//        }
//    }

    /**
     * Called when the player enters the boundaries of the tile
     *
     * @param col    column index of the tile that the player has moved onto
     * @param row    row index of the tile that the player has moved onto
     * @param player reference to player
     * @param event
     */
    @Override
    public void notifyEvent(float col, float row, TileEventObservable player, TileEventObserver event) {
        //if tundra
        if (col < 0 && row > 0 && currentBiome != "tundra") {
            setMaxVolume(0.1f);
            playGameMusic(tundraST);
            setBiome("tundra");
        }
        //If the tile is in the north east make it swamp
        if (col >= 0 && row > 0 && currentBiome != "swamp") {
            setMaxVolume(1f);
            playGameMusic(swampST);
            setBiome("swamp");
        }
        //If the tile is in the south west make it desert
        if (col >= 0 && row <= 0 && currentBiome != "desert") {
            setMaxVolume(1f);
            playGameMusic(desertST);
            setBiome("desert");
        }
        //If the tile is in the south east make it volcanic
        if (col < 0 && row <= 0 && currentBiome != "volcanic") {
            setMaxVolume(1f);
            playGameMusic(volcanicST);
            setBiome("volcanic");
        }
    }

    /**
     * On tick is called periodically (time dependant on the world settings).
     *
     * @param tick Current game tick
     */
    @Override
    public void onTick(long tick) {
        if(music != null) {
            switch(currentState) {
                case("STATE_EJECTED"):
                    break;
                case("STATE_LOOPING"):
                    if(!music.isLooping()) {
                        music.setLooping(true);
                        music.play();
                    }
                    break;
                case("STATE_STOPPED"):
                    if(music.isLooping() || music.isPlaying()) {
                        music.stop();
                    }
                    break;
                case("STATE_PAUSED"):
                    if(music.isLooping() || music.isPlaying()) {
                        music.pause();
                    }
                    break;
                case("STATE_PLAYING"):
                    if(!music.isPlaying()) {
                        music.play();
                    }
                    break;
                case("STATE_FADEOUT"):
                    if(music.isLooping() || music.isPlaying()) {
                        if(volume > 0.00f)
                            volume -= delta * fadeTime;
                        else {
                            volume = 0.0f;
                            setState("STATE_STOPPED");
                        }
                        music.setVolume(Math.abs(volume));
                    }
                    break;
                case("STATE_FADEIN"):
                    if(music.isLooping() || music.isPlaying()) {
                        if(volume < maxVolume) {
                            volume += delta * fadeTime;
                        } else {
                            volume = maxVolume;
                            setState(previousState);
                        }
                        music.setVolume(volume);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
