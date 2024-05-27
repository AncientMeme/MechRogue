package deco2800.thomas.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayerStatsTest {

    private PlayerPeon testPlayer;

    @Before
    public void setup() {
        testPlayer = new PlayerPeon(0,0,0,20);
    }

    @Test
    public void getStrengthTest() {
        Assert.assertEquals(5, testPlayer.getStrength());
    }

    @Test
    public void setStrengthTest() {
        testPlayer.setStrength(10);
        Assert.assertEquals(10, testPlayer.getStrength());
    }

    @Test
    public void getAttackDamageTest() {
        Assert.assertEquals(7, testPlayer.getAttackDamage());
    }

    @Test
    public void getDefenseTest() {
        Assert.assertEquals(0, testPlayer.getDefense());
    }

    @Test
    public void setDefenseTest() {
        testPlayer.setDefense(10);
        Assert.assertEquals(10, testPlayer.getDefense());
    }

    @Test
    public void getSpeedTest() {
        Assert.assertEquals(0, testPlayer.getSpeed(), 0.01);
    }

    @Test
    public void setSpeedTest() {
        testPlayer.setSpeed(0.1f);
        Assert.assertEquals(0.1f, testPlayer.getSpeed(), 0.01);
    }

    @Test
    public void getStatPointTest() {
        Assert.assertEquals(0, testPlayer.getStatPoints());
    }

    @Test
    public void setStatPointTest() {
        testPlayer.setStatPoints(20);
        Assert.assertEquals(20, testPlayer.getStatPoints());
    }

    @Test
    public void getStatPointTestAfterLevelUp() {
        testPlayer.levelUp();
        Assert.assertEquals(3, testPlayer.getStatPoints());
    }

    @Test
    public void getStatPointTestAfterMultipleLevelUp() {
        for (int i = 0; i < 4; i++) {
            testPlayer.levelUp();
        }
        Assert.assertEquals(12, testPlayer.getStatPoints());
    }

    @Test
    public void setNegativeStatPointTest() {
        testPlayer.setStatPoints(-20);
        Assert.assertEquals(0, testPlayer.getStatPoints());
    }
}
