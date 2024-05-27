package deco2800.thomas.worlds;
import deco2800.thomas.entities.AbstractEntity;
import deco2800.thomas.entities.WorldBorder;
import deco2800.thomas.util.SquareVector;
import deco2800.thomas.util.Vector2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * World based collision map that will be used to efficiently test for collisions
 */
public class WorldCollisionMap implements Serializable {
    // Width & Height of collision map - is static
    private int width;
    private int height;

    // Indexed hashmap to store abstract entities at each cell
    AbstractEntity[] internalCollisionMap;

    /**
     * Creates world collision map with static width and height
     * @param width width of collision map
     * @param height height of collision map
     */
    public WorldCollisionMap(int width, int height) {
        this.width = width;
        this.height = height;
        internalCollisionMap = new AbstractEntity[width * height];
        // Initialise with null
        for (int i = 0; i < width * height; i++) {
            internalCollisionMap[i] = null;
        }
    }

    /**
     * Registers the entity e in the collision map at x, y
     * @param x
     * @param y
     * @param e entity to add to the collision map
     */
    public void addCollisionEntry(int x, int y, AbstractEntity e)
            throws IndexOutOfBoundsException {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            int index = y * width + x;
            internalCollisionMap[index] = e;
        } else {
            // position out of bounds
            System.out.println(String.format("X: %d, Y: %d", x, y));
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Clears the collision map at x, y
     * @param x
     * @param y
     */
    public void clearCollisionEntry(int x, int y) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            int index = y * width + x;
            internalCollisionMap[index] = null;
        }
    }

    /**
     * Gets the registered collision entity in the collision map
     * at x, y
     * @param x
     * @param y
     * @return abstract entity if found, else null
     */
    public AbstractEntity getCollisionEntry(int x, int y) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            int index = y * width + x;
            return internalCollisionMap[index];
        } else {
            return new WorldBorder();
        }
    }

    /**
     * Returns registered collision positions. Used for debug rendering
     */
    public List<SquareVector> getRegisteredCollisionPositions() {
        List<SquareVector> collisionPositions = new ArrayList<>();
        // Add all actual collisions
        for (int index = 0; index < width * height; index++) {
            if (internalCollisionMap[index] != null) {
                int x = index % width;
                int y = (index - x) / height;
                collisionPositions.add(new SquareVector((float)x, (float)y));
            }
        }
        // Add world border
        for (int x = -1; x < width + 1; x++) {
            for (int y = -1; y < width + 1; y++) {
                if (!(x >= 0 && y >= 0 && x < width && y < height)) {
                    collisionPositions.add(new SquareVector((float)x, (float)y));
                }
            }
        }

        return collisionPositions;
    }

    /**
     * Returns size of collision map
     */
    public Vector2 getSize() {
        return new Vector2(width, height);
    }
}
