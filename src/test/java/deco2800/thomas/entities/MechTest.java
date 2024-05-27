package deco2800.thomas.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MechTest {
    private Mech testMech;
    private PlayerPeon p;

    @Before
    public void setUp() {
        testMech = new Mech(5, 5, 0.2f, 5);
        // PlayerPeon to test behaviour in different MechStates
        p = new PlayerPeon(1, 1, 1, 20);
    }

    @Test
    public void getStrength() {
        Assert.assertEquals(5, testMech.getStrength());
    }

    @Test
    public void getDefense() {
        Assert.assertEquals(5, testMech.getDefense());
    }

    @Test
    public void getSpeed() {
        Assert.assertEquals(0.2f, testMech.getSpeedDeduction(), 0.0f);
    }

    @Test
    public void getHealth() {
        Assert.assertEquals(5, testMech.getHealth());
    }

    @Test
    public void getMaxHealth() {
        Assert.assertEquals(5, testMech.getMaxHealth());
    }

    @Test
    public void setHealth() {
        //Normal Case
        testMech.setHealth(3);
        Assert.assertEquals(3, testMech.getHealth());
        //Exceeding maxHealth
        testMech.setHealth(10);
        Assert.assertEquals(5, testMech.getHealth());
        //Value less than 0
        testMech.setHealth(-5);
        Assert.assertEquals(0, testMech.getHealth());
    }
}