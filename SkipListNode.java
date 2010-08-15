/**
 * @file SkipListNode.java
 * @author Andrew De Ponte
 * @brief SkipListNode implementation class file.
 *
 * A file containing the implementation of a skip list node. This skip
 * list node is specifically designed as a component of a skip list
 * applet.
 */
 
import java.lang.Integer;

/**
 * A class implementation of a skip list node.
 *
 * A class designed to implement a skip list node specifically designed
 * to be a component of a skip list which is associated with skip list
 * applet. This skip list applet is designed to allow users to interact
 * with the skip list and learn how skip lists work.
 */
class SkipListNode {
    Integer key;
    SkipListNode nodeAfter;
    SkipListNode nodeBefore;
    SkipListNode nodeAbove;
    SkipListNode nodeBelow;

    // These two variables only exist for the drawing algorithm. They
    // are filled in as the Drawing algorithm goes through to allow
    // relative coordinate calculations in combination with the drawing
    // algorithm. The idea is that the drawing algorithm starts at the
    // bottom left and draws towers from left to right. This allows
    // calculations of the x and y coordinates of each node in the
    // towers on the left to be calculated before reaching the right
    // towers. Hence, once at the right towers you can grab the
    // coordinates from the node before it and use them to calculate the
    // needed information to draw the horizontal connecting lines.
    int xPos;
    int yPos;
    boolean touched;
    boolean vtouched; // if it was touched by vertical traversal
    boolean rtouched; // if node connection was broken

    /**
     * Construct skip list node.
     *
     * Construct an empty skip list node.
     */
    SkipListNode() {
        nodeAfter = null;
        nodeBefore = null;
        nodeAbove = null;
        nodeBelow = null;
    }

    /**
     * Construct a skip list node.
     *
     * Construct a skip list node and set it's associated key to the
     * provided key.
     */
    SkipListNode(int keyVal) {
        nodeAfter = null;
        nodeBefore = null;
        nodeAbove = null;
        nodeBelow = null;
        key = new Integer(keyVal);
    }
}
