package deco2800.thomas.worlds;

import deco2800.thomas.entities.Rock;
import deco2800.thomas.entities.Tree;
import deco2800.thomas.util.SquareVector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractWorldTest {
    TestWorld testWorld;
    Tree tree;
    Rock rock;

    @Before
    public void setUp() {
        testWorld = new TestWorld();

        //add a tree at row 3 column 5
        tree = new Tree(testWorld.getTile(3,5), true);
        testWorld.addEntity(tree);

        //add a rock at row -1 column 3
        rock = new Rock(testWorld.getTile(-1, 3), true);
        testWorld.addEntity(rock);
    }

    @Test
    public void testGetTree() {
        Assert.assertEquals(tree.getEntityID(), testWorld.getEntity(new SquareVector(3, 5))
                .getEntityID());
        Assert.assertNull(testWorld.getAgentEntity(new SquareVector(3, 5)));
    }

    @Test
    public void testGetNothing() {
        Assert.assertEquals(null, testWorld.getEntity(new SquareVector(24, 24)));
        Assert.assertEquals(null, testWorld.getAgentEntity(new SquareVector(24, 24)));
    }

    @Test
    public void testGetRock() {
        Assert.assertEquals(rock.getEntityID(), testWorld.getEntity(new SquareVector(-1, 3))
                .getEntityID());
        Assert.assertNull(testWorld.getAgentEntity(new SquareVector(-1, 3)));
    }
}