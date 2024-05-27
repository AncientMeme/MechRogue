package deco2800.thomas.entities;

import deco2800.thomas.GdxTestRunner;
import deco2800.thomas.entities.enemies.EnemyPeon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class DamageIndicatorTest {
    private PlayerPeon player;
    private EnemyPeon enemy;

    @Before
    public void before() {
        // Initialise a player and enemy, within one column and row from each other
        // so they can attack each other without problem
        player = new PlayerPeon(5, 5, 10, 20);
        enemy = new EnemyPeon(5, 5, 5, 40, 5, 5, 5, 1);
    }

    @Test
    public void createsDamageIndicatorWhenThereIsDamage() {
        player.attack(enemy, 5);
        // Checks an indicator exists, and is the right amount
        Assert.assertEquals(-5, enemy.getDamageIndicators().get(0).getAmount());
        player.attack(enemy, 10);
        // Test again for good measure
        Assert.assertEquals(-10, enemy.getDamageIndicators().get(1).getAmount());
    }

    @Test (expected = NullPointerException.class)
    public void removesDamageIndicatorWhenExpired()  {
        // First tick indicator should be dead is 61
        player.attack(enemy, 5);
        for (int i = 0; i < 61; i++) {
            enemy.tickIndicators();
        }
        // Should give null pointer exception (as list should be empty)
        enemy.getDamageIndicators().get(0);
    }

    @Test
    public void aliveDamageIndicatorAtLastTick()  {
        player.attack(enemy, 5);
        // Last tick the indicator would be alive is 59
        for (int i = 0; i < 59; i++) {
            enemy.tickIndicators();
        }
        // Should be not null (as an indicator should exist)
        Assert.assertNotNull(enemy.getDamageIndicators().get(0));
    }
}
