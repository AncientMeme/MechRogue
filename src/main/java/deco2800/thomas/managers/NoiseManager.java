package deco2800.thomas.managers;

import deco2800.thomas.GameScreen;
import deco2800.thomas.util.OpenSimplexNoise;

/**
 * Manager that handles noisy stuff
 */
public class NoiseManager extends AbstractManager {

    private OpenSimplexNoise noise;

    private static NoiseManager instance = null;

    /**
     * create a instance that controls the screen to be displayed
     * @return
     */
    public static NoiseManager get(){
        if (instance == null) {
            // create a new noise manager
            instance = new NoiseManager();
        }
        return instance;
    }

    public NoiseManager() {
        noise = new OpenSimplexNoise(1337);
    }

    /**
     * Sets the seed of the noise generation
     * @param seed seed
     */
    public void setSeed(long seed) {
        noise.reset(seed);
    }

    /**
     * Sample the simplex noise at point x, y
     * @param x
     * @param y
     * @return [-1, 1] simplex noise value at x, y
     */
    public float simplex2D(float x, float y) {
        return (float)noise.noise2(x, y);
    }

    /**
     * Sample the simplex noise at point x, y with multisample
     * octaves applied
     * @param x
     * @param y
     * @param octaves number of octaves (recommend 4)
     * @param lacunarity lacunarity of the octave noise (recommend 2.0)
     * @param persistence persistence of the octave noise - falloff of amplitude (recommend 0.5)
     * @return octave simplex noise at point x, y
     */
    public float simplexOctaves2D(float x, float y, int octaves, float lacunarity, float persistence) {
        float totalSum = 0.0f;
        float val = 0.0f;
        float frequency = 1.0f;
        float amplitude = 1.0f;

        for (int i = 0; i < octaves; i++) {
            val += simplex2D(x * frequency, y * frequency) * amplitude;
            totalSum += amplitude;
            amplitude *= persistence;
            frequency *= lacunarity;
        }

        // Map into -1 to 1
        if (totalSum != 0) {
            val = val / totalSum;
        }

        return val;
    }

    /**
     * Much like simplex2D, except this function returns a result in the range
     * min to max
     * @param x x value to sample
     * @param y y value to sample
     * @param min min value to map to
     * @param max max value to map to
     * @return simplex noise sampled at point x, y in range [min, max]
     */
    public float simplex2DRange(float x, float y, float min, float max) {
        // assuming simplex2D returns -1 to 1
        float val = simplex2D(x, y);
        // Map to 0-1
        val = val / 2.0f + 0.5f;
        // Map to min-max
        val = min + (max - min) * val;
        return val;
    }

    /**
     * Sample the simplex noise at point x, y with multisample
     * octaves applied, mapped into the range min, max
     * @param x x to sample
     * @param y y to sample
     * @param octaves number of octaves (recommend 4)
     * @param lacunarity lacunarity of the octave noise (recommend 2.0)
     * @param persistence persistence of the octave noise - falloff of amplitude (recommend 0.5)
     * @param min min value to map to
     * @param max max value to map to
     * @return octave simplex noise at point x, y mapped to the range [min, max]
     */
    public float simplexOctaves2DRange(float x, float y, int octaves, float lacunarity, float persistence, float min, float max) {
        // assuming simplex2D returns -1 to 1
        float val = simplexOctaves2D(x, y, octaves, lacunarity, persistence);
        // Map to 0-1
        val = val / 2.0f + 0.5f;
        // Map to min-max
        val = min + (max - min) * val;
        return val;
    }
}

