package deco2800.thomas.managers;

import java.io.Serializable;

/**
 * Object to handle a single instance of a sprite sheet animation.
 * Handles playing a sprite sheet for the renderer to interact with.
 * Is stored per entity that has an animation.
 */
public class SpriteSheetAnimation implements Serializable {
    // Spritesheet map identifier
    private String spritesheet;
    // Current animation frame
    private int currentFrame;
    // Time between each frame
    private float timeBetweenFrames;
    // Internal counter to determine when to switch
    private float timeUntilNextFrame;

    /**
     * Constructs a sprite sheet animation with the given options
     * @param spriteSheetName spritesheet map identifier defined in TextureManager
     * @param timeBetweenFrames time between swaps in frames
     */
    public SpriteSheetAnimation(String spriteSheetName, float timeBetweenFrames) {
        this.spritesheet = spriteSheetName;
        this.timeBetweenFrames = timeBetweenFrames;
        this.timeUntilNextFrame = timeBetweenFrames;
        this.currentFrame = 0;
    }

    /**
     * Update the animation
     * @param deltaTime time between animation updates
     */
    public void tickAnimation(float deltaTime) {
        timeUntilNextFrame -= deltaTime;
        if (timeUntilNextFrame <= 0) {
            currentFrame++;
            timeUntilNextFrame = timeBetweenFrames;

            // Manage wraparound
            TextureManager tm = GameManager.get().getManager(TextureManager.class);
            int totalFrames = tm.getSpritesheet(spritesheet).getCellCount();
            if (currentFrame == totalFrames) {
                currentFrame = 0;
            }
        }
    }

    /**
     * return the sprite sheet identifier to use for indexing in the TextureManager
     */
    public String getSpritesheetIdentifier() {
        return spritesheet;
    }

    /**
     * Get current animation frame
     */
    public int getAnimationFrame() {
        return currentFrame;
    }

    /**
     * Reset the current animation so it starts at the first frame
     * */
    public void resetAnimationFrame() {
        timeUntilNextFrame = timeBetweenFrames;
        currentFrame = 0;
    }
}
