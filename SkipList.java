/**
 * @file SkipList.java
 * @author Andrew De Ponte
 * @brief SkipList implementation class file.
 *
 * A file containing an implementation of a SkipList. The implementation
 * of the skip list in this file contains no support for actually
 * storing data along with the nodes. It is specifically designed to
 * be part of a SkipList applet which allows for use of the skip list as
 * a tool for learning how skip lists are implemented and work. Hence,
 * the class contains code to handle the graphics of visually displaying
 * the skip list as well.
 */

import java.lang.Integer;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Color;

/**
 * A class to implement a skip list.
 *
 * A class designed to implement skip list specifically for the purpose
 * of training people how skip lists work. This does not mean that it is
 * some sort of template class. It rather means that this class is part
 * of a larger Skip List applet which allows for people to interact with
 * the skip list and learn how skip lists actually work.
 */
public class SkipList {
    // These are the level 0 negetive and posetive infinit nodes.
    private SkipListNode levZeroNegInfNode;
    private SkipListNode levZeroPosInfNode;

    // These are used to keep track of basically the neg infinity and
    // posetive infinity nodes that are at the top level. Hence, the top
    // left node is actually topLevNegInfNode.
    private SkipListNode topLevNegInfNode;
    private SkipListNode topLevPosInfNode;

    // This is used to keep track of the number of levels existing for
    // the skip list.
    private int totalHeight;
    private int nodeWidth, nodeHeight, hNodeSpace, vNodeSpace;

    /**
     * Construct an empty SkipList.
     *
     * Create an empty SkipList. That is a skip list with only a
     * negative infinity node and a posetive infinity node on the zero
     * level.
     */
    public SkipList() {
        levZeroNegInfNode = null;
        levZeroPosInfNode = null;
        totalHeight = 0;
        nodeWidth = 40;
        nodeHeight = 20;
        hNodeSpace = 30;
        vNodeSpace = 20;
    
        AddEmptyLevelAbove();
    }
    
    /**
     * Search for node in skip list.
     *
     * Search for a node with the given key in the skip list. This is
     * the general search for the skip list and would be used when the
     * skip list was used to implement a dictionary or the like.
     * @param k The key value to search for.
     * @return The node with matching key, or null.
     * @retval null Failed to find node with the given key.
     */
    public SkipListNode Find(int k) {
        SkipListNode p, q;
        Integer key;

        key = new Integer(k);

        p = topLevNegInfNode;
        q = null;

        do {
            p.touched = true;
            p.vtouched = true;
            while (p.nodeAfter.key.intValue() <= key.intValue()) {
                p.nodeAfter.touched = true;
                if (p.nodeAfter.key.equals(key)) {
                    return p.nodeAfter;
                }
                p = p.nodeAfter;
            }
            
            p.nodeAfter.touched = true;
            q = p;
            p = p.nodeBelow;
        } while (p != null);

        return null;
    }

    /**
     * Remove a key from the skip list.
     *
     * The Remove function removes all instances of nodes with the given
     * key from the skip list.
     * @param k The key of the nodes you would like to remove.
     * @return The true/false depending on success or failure.
     * @retval true Successfully removed key from the skip list.
     * @retval false Failed to find the given key in the skip list.
     */
    public boolean Remove(int k) {
        SkipListNode p;

        p = Find(k);
        if (p == null) {
            return false;
        }

        // If I get this far then I know that I have the highest node
        // with the given key. This allows me to work my way down the
        // tower removing the nodes and connecting their neighbors
        // properly.

        while (p != null) {
            p = RemoveNode(p);
        }

        return true;
    }
    
    /**
     * Insert a node into the skip list.
     *
     * Insert a node into the skip list given a key value. Note: If a
     * node exist with the provided key already a new node will not be
     * inserted.
     * @param k The key value to use for the node to insert.
     * @return The node that was just inserted, or null if failure.
     * @retval null A node with the provided key already exists.
     */
    public SkipListNode Insert(int k) {
        SkipListNode p, q, newNode;
        Random rand;

        rand = new Random();

        newNode = new SkipListNode(k);
        
        p = InsertSearch(k);

        // If I find a node which already has the key then don't perform
        // the insert.
        if (p.key.equals(newNode.key)) {
            return null;
        }

        // If I make it this far I know that p is the largest keyed node
        // less than the key requested for insert, and it is on level
        // zero of the structure.

        // That means the new node needs to be inserted into the skip
        // list.

        // Insert new node in proper place on level zero.
        q = InsertAfter(p, newNode);

        // Loop through seeing if heads or tails is received from
        // flipping the coin. If the heads is received then I want to
        // insert the newNode in the next level above. If the tails is
        // received then I am done and don't want to insert it on the
        // next level up. Note: true = heads, false = tails.
        while (rand.nextBoolean() == true) {
            System.out.println("Coin landed HEADs side up.");
            while ((p.nodeAbove == null) && !(IsNegInfNode(p))) {
                p = p.nodeBefore;
            }

            if (IsNegInfNode(p) && (p.nodeAbove == null)) {
                AddEmptyLevelAbove();
            }

            // Move p up to the next level
            p = p.nodeAbove;

            // Insert another node with the new key in this level after
            // p and above q (the previously inserted node).
            q = InsertAfterAbove(p, q, k);
        }

        System.out.println("Stopped flipping coin because hit TAILs.");

        return q;
    }

    /**
     * Obtain the number of levels.
     *
     * Obtain the number of levels that exist within the skip list.
     * Note: Level 0 counts as one of the levels.
     * @return Number of levels in the skip list.
     */
    public int GetNumLevels() {
        return totalHeight;
    }
    
    /**
     * Clear the touched flags.
     *
     * The ClearTouchedFlag function resets all the touched flags to not
     * touched. This allows for graphically displaying the path of the
     * algorithms. If this is not called in between calls to diffierent
     * algorithms then the touch flags will carry over and it will not
     * correctly give a graphical representation of the algorithm path.
     */
    public void ClearTouchedFlag() {
        SkipListNode curBaseNode, curNode;

        curBaseNode = levZeroNegInfNode;

        while (curBaseNode != null) {
            curBaseNode.touched = false;
            curBaseNode.vtouched = false;
            curBaseNode.rtouched = false;
        
            curNode = curBaseNode.nodeAbove;
        
            while (curNode != null) {
                curNode.touched = false;
                curNode.vtouched = false;
                curNode.rtouched = false;
                
                curNode = curNode.nodeAbove;
            }
        
            curBaseNode = curBaseNode.nodeAfter;
        }
    }
 
    /**
     * Check if node is negative infinity node.
     *
     * Determine if the passed node is a negative infinity node.
     * @return Value signifying if the node is or is not neg inf node.
     * @retval true The node IS a negative infinity node.
     * @retval false The node IS NOT a negative infinity node.
     */
    private boolean IsNegInfNode(SkipListNode node) {
        Integer key;

        key = Integer.MIN_VALUE;

        if (node.key.equals(key)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if node is posetive infinity node.
     *
     * Determine if the passed node is a posetive infinity node.
     * @return Value signifying if the node is or is not pos inf node.
     * @retval true The node IS a posetive infinity node.
     * @retval false The node IS NOT a posetive infinity node.
     */
    private boolean IsPosInfNode(SkipListNode node) {
        Integer key;

        key = Integer.MAX_VALUE;

        if (node.key.equals(key)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Remove a single instance of a node.
     *
     * The RemoveNode function only removes an instance of a node on a
     * single level. It is designed to be used by the Remove function.
     * This function removes a node from a level and returns the node
     * that exists below it so that it can be removed next in the Remove
     * function.
     * @param node The node to remove.
     * @return The node below the node being removed, or null if none.
     * @retval null There exist no more nodes below this node.
     */
    private SkipListNode RemoveNode(SkipListNode node) {
        SkipListNode before;
        SkipListNode after;
        
        before = node.nodeBefore;
        after = node.nodeAfter;
        
        before.rtouched = true;
        after.rtouched = true;
        
        node.nodeAbove = null;

        before.nodeAfter = after;
        after.nodeBefore = before;
        
        return node.nodeBelow;
    }
  
    /**
     * Add an empty level to the top.
     *
     * Add an empty level to the top of the skip list structure. An
     * empty level only consists of a negative infinity node and a
     * posetive infinity node.
     */
    private void AddEmptyLevelAbove() {
        if ((levZeroNegInfNode == null) && (levZeroPosInfNode == null)) {
            levZeroNegInfNode = new SkipListNode(Integer.MIN_VALUE);
            levZeroPosInfNode = new SkipListNode(Integer.MAX_VALUE);
            
            // Using Integer.MIN_VALUE as the negative infinit and
            // Integer.MAX_VALUE as the posetive infinit. Hence, I am
            // reducing the set of acceptable intergers for keys by two.
            levZeroNegInfNode.nodeAfter = levZeroPosInfNode;
            levZeroPosInfNode.nodeBefore = levZeroNegInfNode;

            topLevNegInfNode = levZeroNegInfNode;
            topLevPosInfNode = levZeroPosInfNode;
        } else {
            SkipListNode newNegInfNode;
            SkipListNode newPosInfNode;

            newNegInfNode = new SkipListNode(Integer.MIN_VALUE);
            newPosInfNode = new SkipListNode(Integer.MAX_VALUE);

            newNegInfNode.nodeAfter = newPosInfNode;
            newPosInfNode.nodeBefore = newNegInfNode;

            newNegInfNode.nodeBelow = topLevNegInfNode;
            newPosInfNode.nodeBelow = topLevPosInfNode;

            topLevNegInfNode.nodeAbove = newNegInfNode;
            topLevPosInfNode.nodeAbove = newPosInfNode;

            topLevNegInfNode = newNegInfNode;
            topLevPosInfNode = newPosInfNode;
        }

        totalHeight = totalHeight + 1;
    }

    /**
     * Search for relative insert node.
     *
     * This function searches for a node to use for a relative insert.
     * This function is NOT designed to be used to find a node with a
     * given k. Note: This function can return a node which has the same
     * key as the key that was searched for. This should be checked for
     * and if it is the case an insert should NOT be performed.
     * @param k The key value to search for (norm key of node to add).
     * @return The largest keyed node less than or equal to key.
     */
    private SkipListNode InsertSearch(int k) {
        SkipListNode p, q;
        Integer key;

        key = new Integer(k);
        
        p = topLevNegInfNode;
        q = null;

        do {
            p.touched = true;
            p.vtouched = true; // this will cause the top left to be set
                               // when it shouldn't be but it should
                               // still work fine because the one below
                               // it won't be touched unless there is
                               // vertical association.
            while (p.nodeAfter.key.intValue() <= key.intValue()) {
                p.nodeAfter.touched = true;
                p = p.nodeAfter;
                
            }
            p.nodeAfter.touched = true;

            q = p;
            p = p.nodeBelow;
        } while (p != null);
        
        return q;
    }


    /**
     * Insert node after.
     *
     * The InsertAfter function is designed to allow one to insert a
     * node relative to another node. Specifically after a node which is
     * provided. This only handles the horizontal insertion of nodes. It
     * does NOT handle the vertical insertion.
     * @param after Node to place the new node after.
     * @param node The new node to place after the provide after node.
     * @return The node that was just inserted.
     */
    private SkipListNode InsertAfter(SkipListNode after, SkipListNode node) {
        // Set the references for the before and after nodes and the new
        // node.
        node.nodeBefore = after;
        node.nodeAfter = after.nodeAfter;
        after.nodeAfter.nodeBefore = node;
        after.nodeAfter = node;

        return node;
    }

    /**
     * Insert node after and above.
     *
     * The InsertAfterAbove function is designed to allow one to insert
     * a node relative to a node before it and a node below it. This is
     * used for adding nodes to levels other than the zero level.
     * @param after Node to place the new node after.
     * @param above Node to place the new node above.
     * @param k The key of the new node.
     * @return The new node that was just added.
     */
    private SkipListNode InsertAfterAbove(SkipListNode after,
        SkipListNode above, int k) {

        SkipListNode newNode, p;

        newNode = new SkipListNode(k);

        p = InsertAfter(after, newNode);

        // Rewang the pointers (really references) from the node below
        // to point to the node that was just added.
        above.nodeAbove = p;
        p.nodeBelow = above;

        return p;
    }
   
    /**
     * Draw the skip list.
     *
     * The DrawSkipList function draws the entire skip list. It also
     * highlights the path which the last called algorithm followed
     * through the nodes.
     * @param g The Graphics class from the Canvas to draw on.
     */
    public void DrawSkipList(Graphics g) {
        SkipListNode curBaseNode, curNode;
       
        int baseNodeXPos, baseNodeYPos, numLevels;
        int curNodeXPos, curNodeYPos;
        
        baseNodeXPos = 0;
        baseNodeYPos = 0;
        curNodeXPos = 0;
        curNodeYPos = 0;

        numLevels = GetNumLevels();

        // Calculate the starting Y position for the base nodes.
        baseNodeYPos = ((numLevels - 1) * (nodeHeight + vNodeSpace));
        
        curBaseNode = levZeroNegInfNode;

        while (curBaseNode != null) {
            DrawNode(curBaseNode, g, baseNodeXPos, baseNodeYPos);

            curNode = curBaseNode.nodeAbove;

            curNodeXPos = baseNodeXPos;
            curNodeYPos = baseNodeYPos - vNodeSpace - nodeHeight;

            while (curNode != null) {
                DrawNode(curNode, g, curNodeXPos, curNodeYPos);
                
                curNodeYPos = curNodeYPos - vNodeSpace - nodeHeight;
                curNode = curNode.nodeAbove;
            }
            
            baseNodeXPos = baseNodeXPos + nodeWidth + hNodeSpace;
            curBaseNode = curBaseNode.nodeAfter;
        }
    }

    /**
     * Draw a node of the skip list.
     *
     * Draw a specific node of the skip list handling highlighting of
     * the node and drawing of node connection lines.
     * @param node The node to draw.
     * @param g The Graphics class from the Canvas to draw on.
     * @param xpos The upper left x coordinate of the node.
     * @param ypos The upper left y coordinate of the node.
     */
    private void DrawNode(SkipListNode node, Graphics g,
        int xpos, int ypos) {
        
        String foo;
        
        // Draw the node rectangle
        g.drawRect(xpos, ypos, nodeWidth, nodeHeight);

        // Check if the node is touched and if it is then draw it bolder
        // than the other nodes.
        if (node.touched == true) {
            g.drawRect(xpos+1, ypos+1, nodeWidth-2, nodeHeight-2);
            g.drawRect(xpos+2, ypos+2, nodeWidth-4, nodeHeight-4);
        }
      
        // Store the nodes x and y position.
        node.xPos = xpos;
        node.yPos = ypos;
       
        // Draw the node key
        if (IsNegInfNode(node)) {
            foo = new String("- Inf");
        } else if (IsPosInfNode(node)) {
            foo = new String("+ Inf");
        } else {
            foo = new String(node.key.toString());
        }
        g.drawString(foo, xpos + 5, ypos + 15);

        // Draw node horizontal connecting line
        if (node.nodeBefore != null) {
            g.drawLine(xpos,
                       (ypos + (nodeHeight/2)),
                       (node.nodeBefore.xPos + nodeWidth),
                       (ypos + (nodeHeight/2)));
            // Check if the connection has been touched, if so draw a
            // bolder connecting line.
            if ((node.rtouched) && (node.nodeBefore.rtouched)) {
                g.setColor(Color.red);
                g.drawLine(xpos,
                       (ypos + (nodeHeight/2) - 1),
                       (node.nodeBefore.xPos + nodeWidth),
                       (ypos + (nodeHeight/2)) - 1);
                g.drawLine(xpos,
                       (ypos + (nodeHeight/2) + 1),
                       (node.nodeBefore.xPos + nodeWidth),
                       (ypos + (nodeHeight/2)) + 1);
                g.setColor(Color.black);
            } else if ((node.touched) && (node.nodeBefore.touched)) {
                g.drawLine(xpos,
                       (ypos + (nodeHeight/2) - 1),
                       (node.nodeBefore.xPos + nodeWidth),
                       (ypos + (nodeHeight/2)) - 1);
                g.drawLine(xpos,
                       (ypos + (nodeHeight/2) + 1),
                       (node.nodeBefore.xPos + nodeWidth),
                       (ypos + (nodeHeight/2)) + 1);
            }
        }

        // Draw node vertical connecting line
        if (node.nodeAbove != null) {
            g.drawLine((xpos + (nodeWidth/2)),
                       (ypos),
                       (xpos + (nodeWidth/2)),
                       (ypos - vNodeSpace));
            if (node.vtouched && node.nodeAbove.touched) {
                g.drawLine((xpos + (nodeWidth/2) - 1),
                       (ypos),
                       (xpos + (nodeWidth/2) - 1),
                       (ypos - vNodeSpace));
                g.drawLine((xpos + (nodeWidth/2) + 1),
                       (ypos),
                       (xpos + (nodeWidth/2) + 1),
                       (ypos - vNodeSpace));
            }
        }
    }

}
