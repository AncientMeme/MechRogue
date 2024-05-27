package deco2800.thomas.entities;

import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.managers.GameManager;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ProjectileTest {
    PlayerPeon player;
    @Before
    public void setup() {
        player = new PlayerPeon(1, 1, 1, 3);
    }

    @Test
    public void testMechProjectile() {
        MechProjectile mechProjectile = new MechProjectile(1, 1, 1, 1, 1, 1, player);

        assertEquals(mechProjectile.damage, 1, 0.1);
        assertEquals(mechProjectile.getRow(), 1, 0.1);
        assertEquals(mechProjectile.getCol(), 1, 0.1);
        assertEquals(mechProjectile.getDirection(), 1, 0.1);
    }

    @Test
    public void testMechProjectileNoCollision() {
        MechProjectile mechProjectile = new MechProjectile(3, 3, 1, 1, 1, 1, player);
        EnemyPeon peon = new EnemyPeon(1, 1, 1, 1, 1, 1, 1,1);

        assert(!mechProjectile.collidesWith(peon));
    }

    @Test
    public void testMechProjectileCollision() {
        MechProjectile mechProjectile = new MechProjectile(3, 3, 1, 1, 1, 1, player);
        EnemyPeon peon = new EnemyPeon(3, 3, 1, 1, 1, 1, 1, 1);

        assert(mechProjectile.collidesWith(peon));
    }


}
