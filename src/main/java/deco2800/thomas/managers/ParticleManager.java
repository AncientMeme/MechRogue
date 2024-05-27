package deco2800.thomas.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import deco2800.thomas.GameScreen;
import deco2800.thomas.util.OpenSimplexNoise;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager that handles particles
 */
public class ParticleManager extends AbstractManager {
    private static ParticleManager instance = null;

    int counter;
    private Map<Integer, ParticleEffect> particles;

    private TextureManager textureManager = GameManager.getManagerFromInstance(TextureManager.class);

    /**
     * create a instance that controls the particles
     * @return
     */
    public static ParticleManager get(){
        if (instance == null) {
            // create a new particle manager
            instance = new ParticleManager();
        }
        return instance;
    }

    public ParticleManager() {
        counter = 0;
        particles = new HashMap<>();
    }

    /**
     * Gets the particle effect registered at a certain identifier
     * @param identifier integer identifier returned from addParticleEffect
     * @return particle effect registered at 'identifier'. If it does not exist, returns null
     */
    public ParticleEffect getParticleEffect(int identifier) {
        return particles.get(identifier);
    }

    /**
     * Add a particle effect and returns the new identifier that it belongs to
     * @param particleFilePath the .party file's path for the particle
     * @return identifier of new particle
     */
    public int addParticleEffect(String particleFilePath) {
        counter++;

        ParticleEffect pe = new ParticleEffect();
        pe.load(Gdx.files.internal(particleFilePath), Gdx.files.internal("resources/particle_files/textures"));
        particles.put(counter, pe);

        return counter;
    }

    /**
     * Removes particle effect at 'identifier'
     * @param identifier position to remove from
     */
    public void removeParticleEffect(int identifier) {
        if (identifier != 0) {
            ParticleEffect pe = getParticleEffect(identifier);
            if (pe != null) {
                pe.dispose();
                particles.remove(identifier);
            }
        }
    }
}