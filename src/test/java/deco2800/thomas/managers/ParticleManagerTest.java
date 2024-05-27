package deco2800.thomas.managers;

import deco2800.thomas.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ParticleManagerTest {
        @Test
        public void testSingleton() {
            ParticleManager pm1 = ParticleManager.get();
            ParticleManager pm2 = ParticleManager.get();
            assertEquals(pm1, pm2);
        }

        @Test
        public void testUniqueIdentifier() {
            ParticleManager pm = ParticleManager.get();

            int id1 = pm.addParticleEffect("resources/particle_files/fire.party");
            int id2 = pm.addParticleEffect("resources/particle_files/fire.party");
            assertNotEquals(id1, id2);
        }

        @Test
        public void testGetParticle() {
            ParticleManager pm = ParticleManager.get();

            int id = pm.addParticleEffect("resources/particle_files/fire.party");
            pm.getParticleEffect(id).getEmitters().get(0).setName("changed");
            assertEquals("changed", pm.getParticleEffect(id).getEmitters().get(0).getName());
        }

        @Test
        public void testRemoveParticle() {
            ParticleManager pm = ParticleManager.get();

            int id = pm.addParticleEffect("resources/particle_files/fire.party");
            assertNotEquals(null, pm.getParticleEffect(id));
            pm.removeParticleEffect(id);
            assertNull(pm.getParticleEffect(id));
        }
}
