package deco2800.thomas.entities;

import deco2800.thomas.entities.enemies.EnemyPeon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttackableTest {

    private Peon testPlayer;
    private EnemyPeon testEnemy1;
    private int WEAPON_DAMAGE = 10;

    @Before
    public void setUp() {
        //player peon
        testPlayer = new Peon(0, 0, 0, 100);

        //enemy peon
        testEnemy1 = new EnemyPeon(0, 1, 0, 50, 1, 1, 1, 1);
    }

    @Test
    public void testPositiveAttack() {
        testPlayer.attack(testEnemy1, WEAPON_DAMAGE);
        Assert.assertEquals(40, testEnemy1.getHealth());
    }
}