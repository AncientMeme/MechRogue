package deco2800.thomas.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HasHealthTest {

    private Peon testPeon;

    @Before
    public void setup() {
        // Setup a peon for testing
        testPeon = new Peon(0, 0, 0, 20);

    }

    @Test
    public void testGetHealth() {
        int health = testPeon.getHealth();
        Assert.assertEquals(health, 20);
    }

    @Test
    public void testGetMaxHealth() {
        int maxHealth = testPeon.getMaxHealth();
        Assert.assertEquals(maxHealth, 20);
    }

    @Test
    public void testGetProportionalHealth() {
        // Max Health
        testPeon.setHealth(20);
        Assert.assertEquals(1.00, testPeon.getProportionalHealth(), 0.00);

        // Normal Case
        testPeon.setHealth(12);
        Assert.assertEquals(0.60, testPeon.getProportionalHealth(), 0.00);

        //Exceeding max health
        testPeon.setHealth(30);
        Assert.assertEquals(1.00, testPeon.getProportionalHealth(), 0.00);

        // Less than 0
        testPeon.setHealth(-5);
        Assert.assertEquals(0.00, testPeon.getProportionalHealth(), 0.00);
    }

    @Test
    public void testSetHealth() {
        // Normal Case
        testPeon.setHealth(12);
        int health = testPeon.getHealth();
        Assert.assertEquals(health, 12);

        // Exceeding max limit
        testPeon.setHealth(25);
        int exceedingHealth = testPeon.getHealth();
        Assert.assertEquals(exceedingHealth, 20);

        // Lower than 0
        testPeon.setHealth(-5);
        int zeroHealth = testPeon.getHealth();
        Assert.assertEquals(zeroHealth, 0);
    }

    @Test
    public void testSetMaxHealth() {
        // Normal Case
        testPeon.setMaxHealth(50);
        int maxHealth = testPeon.getMaxHealth();
        Assert.assertEquals(maxHealth, 50);

        // Health is greater than max health
        testPeon.setMaxHealth(10);
        int health = testPeon.getHealth();
        Assert.assertEquals(health, 10);

        // Lower than 0
        testPeon.setMaxHealth(-69);
        int zeroMaxHealth = testPeon.getMaxHealth();
        Assert.assertEquals(zeroMaxHealth, 0);
    }

    @Test
    public void testAddHealth() {
        // Normal Case, both add and take away
        testPeon.addHealth(-5);
        int health = testPeon.getHealth();
        Assert.assertEquals(health, 15);

        testPeon.addHealth(2);
        health = testPeon.getHealth();
        Assert.assertEquals(health, 17);

        // Exceeding max limit
        testPeon.addHealth(69);
        int exceedingHealth = testPeon.getHealth();
        Assert.assertEquals(exceedingHealth, 20);

        // Lower than 0
        testPeon.addHealth(-420);
        int zeroHealth = testPeon.getHealth();
        Assert.assertEquals(zeroHealth, 0);
    }

}
