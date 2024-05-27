package deco2800.thomas.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LevelSystemTest {

    private PlayerPeon testPeon;

    @Before
    public void setup() {
        // Setup a peon for testing
        testPeon = new PlayerPeon(0, 0, 0, 20);
    }

    @Test
    public void testGetExp() {
        int exp = testPeon.getExp();
        Assert.assertEquals(exp, 0);
        testPeon.addExp(100);
        Assert.assertEquals(testPeon.getExp(), 0);
        Assert.assertEquals(testPeon.getLevel(),2);
    }

    @Test
    public void testGetLevel() {
        testPeon.addExp(100);
        Assert.assertEquals(testPeon.getLevel(),2);
        testPeon.addExp(100);
        Assert.assertEquals(testPeon.getLevel(),2);
        testPeon.addExp(100);
        Assert.assertEquals(testPeon.getLevel(),3);
    }

    @Test
    public void testSetExp() {
        // Normal Case
        testPeon.setExp(12);
        int exp = testPeon.getExp();
        Assert.assertEquals(exp, 12);

        // Lower than 0
        testPeon.setExp(-5);
        Assert.assertEquals(12, testPeon.getExp());
    }

    @Test
    public void testSetLevel() {
        // Normal Case
        testPeon.setLevel(50);
        Assert.assertEquals(50, testPeon.getLevel());
    }

    @Test
    public void testSetOverMaxLevel() {
        // Level is greater than max level
        testPeon.setLevel(100);
        Assert.assertEquals(testPeon.getLevel(), 100);
        testPeon.setLevel(101);
        Assert.assertEquals(testPeon.getLevel(), 100);
    }

    @Test
    public void testSetNegativeLevel() {
        // Lower than 0
        testPeon.setLevel(-69);
        Assert.assertEquals(1, testPeon.getLevel());
    }

    @Test
    public void levelUpTest() {
        //Normal Case
        testPeon.levelUp();
        Assert.assertEquals(2, testPeon.getLevel());

        //Exceeding Max Level
        testPeon.setLevel(100);
        testPeon.levelUp();
        Assert.assertEquals(100, testPeon.getLevel());
    }
    @Test
    public void testAddExp() {
        // Normal Case, both add and take away
        testPeon.addExp(-5);
        Assert.assertEquals(0, testPeon.getExp());

        testPeon.addExp(10);
        Assert.assertEquals(10,testPeon.getExp());
        Assert.assertEquals(testPeon.getLevel(),1);

        // Exceeding max limit
        testPeon.setLevel(100);
        testPeon.addExp(1000000);
        Assert.assertEquals(testPeon.getLevel(), 100);
    }

    @Test
    public void testAddMoreExpThanRequired() {
        testPeon.setLevel(1);

        testPeon.addExp(90);
        Assert.assertEquals(90, testPeon.getExp());
        Assert.assertEquals(1, testPeon.getLevel());

        testPeon.addExp(20);
        //Adding 20 makes exp go over level exp threshold (100)
        //Player should level up and exp should return to 0
        Assert.assertEquals(0, testPeon.getExp());
        Assert.assertEquals(2, testPeon.getLevel());
    }

}
