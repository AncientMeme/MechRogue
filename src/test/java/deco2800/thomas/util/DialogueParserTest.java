package deco2800.thomas.util;


import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DialogueParserTest {

    private DialogueParser DP = null;
    String path = "resources/dialogue_trees/testFile.txt";

    @Before
    public void setup() {
        DP = new DialogueParser(path);
    }

    @Test
    public void testParse() {
        try {
            assertEquals("Root element :root" ,DP.parse(path));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsEndTrue() {
        assertEquals(true, DP.isEnd());
    }

    @Test
    public void testIsThereBuffFalse() {
        assertEquals("", DP.isThereBuff());
    }

    @Test
    public void testIsEndFalse() {
        DP.getNextBranch(1);
        assertEquals(false, DP.isEnd());
    }

    @Test
    public void testIsThereBuffTrue() {
        DP.getNextBranch(1);
        assertEquals("none", DP.isThereBuff());
    }

    @Test
    public void testGetNextBranch() {
        DP.getNextBranch(1);
        DP.getNextBranch(0);
        assertEquals("2 : branch 3 dialogue option 2",DP.getDialogueOption(DP.getBranch(1)));
    }
}
