package deco2800.thomas.worlds;

import deco2800.thomas.entities.AbstractEntity;
import deco2800.thomas.entities.Peon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WorldCollisionMapTest {
    WorldCollisionMap wcm;
    AbstractEntity p1;

    @Before
    public void setUp() {
        p1 = new Peon();

        wcm = new WorldCollisionMap(10, 10);
        wcm.addCollisionEntry(5, 5, p1);
        wcm.addCollisionEntry(5, 6, p1);
    }

    @Test
    public void testGetEntry() {
        Assert.assertEquals(p1, wcm.getCollisionEntry(5, 5));
        Assert.assertEquals(p1, wcm.getCollisionEntry(5, 6));
        Assert.assertEquals(null, wcm.getCollisionEntry(5, 7));
    }

    @Test
    public void testClearEntry() {
        wcm.clearCollisionEntry(5, 6);
        Assert.assertEquals(p1, wcm.getCollisionEntry(5, 5));
        Assert.assertEquals(null, wcm.getCollisionEntry(5, 6));
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testOutOfBoundsAdd() {
        wcm.addCollisionEntry(-1 ,-1, p1);
    }

    @Test
    public void testOutOfBoundsGet() {
        AbstractEntity ae = wcm.getCollisionEntry(-1, -1);
        Assert.assertEquals("world_border", ae.getObjectName());
    }
}
