package deco2800.thomas.managers;

import deco2800.thomas.ui.TextBubble;
import deco2800.thomas.util.SquareVector;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DialogueManagerTest {

    /**
     * Checks to make sure that get returns the same instance of DM
     */
    @Test
    public void testCreateInstanceOfDM() {
        DialogueManager DM1 = DialogueManager.get();
        DialogueManager DM2 = DM1.get();
        assertEquals(true, DM1.equals(DM2));
    }

    /**
     * Checks to make sure DM adds the correct dialogue to textBubble
     *
     * TODO: test cant run because textureMap isnt instantiated unless the games
     * running...
     */
//    @Test
//    public void testAddDialogue() {
//        String testString = "Testing for correct String in AddDialogue";
//        DialogueManager DM = DialogueManager.get();
//        TextBubble TB = new TextBubble();
//        DM.addTextBubble(TB);
//        DM.addDialogue(testString);
//        assertEquals(true, TB.getSpeech());
//    }


}