/**
 * @file SkipListCanvas
 * @author Andrew De Ponte
 * @brief File of a skip list canvas implementation.
 *
 * A file containing a canvas implementation specifically designed to be
 * a component of a skip list applet.
 */

import java.awt.*;
import javax.swing.*;

/**
 * A class to implement a canvas.
 *
 * A class designed to provide a canvas area where a skip list can be
 * drawn for the skip list applet. This canvas is designed specefically
 * to be function with the use of Swing and more light weight than the
 * standard Canvas class that is part of awt.
 */
public class SkipListCanvas extends JPanel {

    SkipList mySkipList;

    /**
     * Construct a skip list canvas.
     *
     * Construct a skip list canvas with the default settings.
     */
    public SkipListCanvas() {
        super();
        setVisible(true);
        
        mySkipList = new SkipList();
    }

    /**
     * Paint the skip list canvas.
     *
     * The paint function actually paints the entire widget and the full
     * skip list to the canvas.
     * @param g The Graphics class of the parent object call this.
     */
    public void paint(Graphics g) {
        super.paint(g);
        setBackground(new Color(1.0f, 1.0f, 1.0f));
        mySkipList.DrawSkipList(g);
    }

    /**
     * Obtain preferred size.
     *
     * Obtain the preferred size of the skip list canvas.
     * @return A Dimension object containing the preferred size.
     */
    public Dimension getPreferredSize() {
        return new Dimension(1000,1000);
    }

    /**
     * Obtain minimum size.
     *
     * Obtain the minimum size of the skip list canvas.
     * @return A Dimension object containing the minimum size.
     */
    public Dimension getMinimumSize() {
        return new Dimension(1000, 1000);
    }

    /**
     * Obtain maximum size.
     *
     * Obtain the maximum size of the skip list canvas.
     * @return A Dimension object containing the maximum size.
     */
    public Dimension getMaximumSize() {
        return new Dimension(1000, 1000);
    }
}
