package deco2800.thomas.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Reads XML Dialogue Trees. Is used by Dialogue Manager to get Dialogue text which
 * is then loaded in the text Bubble
 * */
public class DialogueParser {

    static final Logger LOG = Logger.getLogger(DialogueParser.class.getPackage().getName());

    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document doc;
    private File file;
    NodeList currentBranch;
    Node nNode;

    private class BranchNodeList implements NodeList {

        Node[] nodelist;

        public BranchNodeList(Node node1, Node node2) {
            this.nodelist = new Node[]{node1, node2};
        }

        /**
         * Returns the <code>index</code>th item in the collection. If
         * <code>index</code> is greater than or equal to the number of nodes in
         * the list, this returns <code>null</code>.
         *
         * @param index Index into the collection.
         * @return The node at the <code>index</code>th position in the
         * <code>NodeList</code>, or <code>null</code> if that is not a valid
         * index.
         */
        @Override
        public Node item(int index) {
            return this.nodelist[index];
        }

        /**
         * The number of nodes in the list. The range of valid child node indices
         * is 0 to <code>length-1</code> inclusive.
         */
        @Override
        public int getLength() {
            return 0;
        }
    }

    public DialogueParser(String pathname) {
        try {
            parse(pathname);
        } catch (ParserConfigurationException e) {
            LOG.warning("ParserConfigurationException");
        } catch (IOException e) {
            LOG.warning("IOException");
        } catch (SAXException e) {
            LOG.warning("SAXException");
        }
    }

    public String parse(String pathname) throws ParserConfigurationException, IOException, SAXException {
        factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        builder = factory.newDocumentBuilder();
        file = new File(pathname);
        doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        currentBranch = doc.getElementsByTagName("initialBranch");
        return ("Root element :" + doc.getDocumentElement().getNodeName());
    }

    //Dialogue manager calls get text of the first two options (possibly in an array)
    //Player chooses option
    //Dialogue manager sets the Next branch out of the two options
    //Parser returns the next set of options
    //Dialogue manager chooses ...
    //End tag is reached. Tells Dialogue manager it has ended along with end "script"?.
    //The dialogue manager applies that to the player.

    public boolean isEnd() {
        Element element = getBranch(0);
        if(element.getAttribute("end").equals("true")) {
            return false;
        }
        return true;
    }

    public String isThereBuff() {
        Element element = getBranch(0);
        return element.getAttribute("buff");
    }

    /**
     * Gets the dialogue for this branch
     *
     * @return
     */
    public String[] getDialogue() {
        String[] dialogueOptions = new String[3];

        Element element = getBranch(0);
        String question = getQuestion(element);
        dialogueOptions[0] = question;
        String dialogue1 = getDialogueOption(element);
        dialogueOptions[1] = dialogue1;

        Element element2 = getBranch(1);
        String dialogue2 = getDialogueOption(element2);
        dialogueOptions[2] = dialogue2;


        return dialogueOptions;
    }

    //Sets next branch
    public void getNextBranch(Integer choice) {
        Element branchOption = (Element) currentBranch.item(choice);
        Element node1 = (Element) branchOption.getFirstChild().getNextSibling();
        Element node2 = (Element) branchOption.getFirstChild().getNextSibling().getNextSibling().getNextSibling();
        BranchNodeList newBranch = new BranchNodeList(node1, node2);
        currentBranch = newBranch;


    }

    public Element getBranch (int branch) {
        nNode = currentBranch.item(branch);
        Element element = (Element) nNode;
        return element;
    }

    public String getQuestion (Element element) {
        return element.getAttribute("question");
    }

    public String getDialogueOption (Element element) {
        return element.getAttribute("text");
    }


}