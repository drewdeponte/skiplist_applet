/**
 * @file SkipListApplet.java
 * @author Andrew De Ponte
 * @brief A file containing a skip list applet.
 *
 * This file contains an implementation of a skip list applet which is
 * designed to let the user interact with a skip list so that they might
 * learn how skip lists work. It graphically displays the stages of the
 * skip list as different actions are performed on the skip list and
 * also displays the path by which the skip list action based algorithms
 * take by highlighting the paths and the nodes of the skip list.
 */
  
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Canvas;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.*;

/**
 * A skip list applet class.
 *
 * A class implementing a skip list applet to allow users to interact
 * and learn how skip lists work.
 */
public class SkipListApplet extends JApplet implements ActionListener {
    private JTextField keyField;
    private JButton insertButton;
    private JButton removeButton;
    private JButton searchButton;
    private JButton clearPathButton;
    private SkipListCanvas canvas;

    /**
     * Initialize the applet.
     *
     * Setup up and configure and run the applet.
     */
    public void init() {
        // Set the layout of the main applet to the border layout so I
        // can create a lower section for controls and a section for
        // painting above it trivially.
        setLayout(new BorderLayout());
    
        // Create a panel for the lower section of the GUI which will
        // contain all the control interface.
        JPanel controlPanel = new JPanel();
        //controlPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        //controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    
        // Create a panel for the key input for any of the actions. This
        // panel will hold the key input edit box.
        JPanel keyPanel = new JPanel();
        //keyPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel keyFieldLabel = new JLabel("Key:");
        keyPanel.add(keyFieldLabel);
        keyField = new JTextField(3);
        keyPanel.add(keyField);
        
        // Create a panel for the action input. This panel will hold the
        // buttons to perform a number of actions.
        JPanel actionPanel = new JPanel();
        //actionPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        insertButton = new JButton("Insert");
        insertButton.addActionListener(this);
        insertButton.setActionCommand("insert");
        actionPanel.add(insertButton);
        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);
        removeButton.setActionCommand("remove");
        actionPanel.add(removeButton);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchButton.setActionCommand("search");
        actionPanel.add(searchButton);
        clearPathButton = new JButton("Clear Path");
        clearPathButton.addActionListener(this);
        clearPathButton.setActionCommand("clear_path");
        actionPanel.add(clearPathButton);
        JLabel authLabel = new JLabel("Author: Andrew De Ponte");
        actionPanel.add(authLabel);
      
        // Add the key panel and action panel to the control panel.
        controlPanel.add(keyPanel);
        controlPanel.add(actionPanel);

        // Place the control panel at the bottom of the page.
        add(controlPanel, BorderLayout.PAGE_END);

        JPanel canvasPanel = new JPanel();
        canvasPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        canvas = new SkipListCanvas();
        JScrollPane canvasScroll = new JScrollPane();
        canvasScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        canvasScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        canvasScroll.add(canvas);
        canvasScroll.setVisible(true);
        canvasScroll.setViewportView(canvas);
        canvasScroll.setPreferredSize(new Dimension(600, 300));
        canvasPanel.add(canvasScroll);
        

        add(canvasPanel, BorderLayout.CENTER);
    }

    /**
     * A common event handler for the skip list applet.
     *
     * This is a common event handler for the skip list applet and all
     * of it's associated widgets. It is created using the
     * ActionListener interface.
     * @param e An ActionEvent passed to yt by a given widget.
     */
    public void actionPerformed(ActionEvent e) {
        if ("insert".equals(e.getActionCommand())) {
            String text = keyField.getText();
            Integer foo = new Integer(text);
            canvas.mySkipList.ClearTouchedFlag();
            canvas.mySkipList.Insert(foo.intValue());
            canvas.repaint();
        } else if ("remove".equals(e.getActionCommand())) {
            String text = keyField.getText();
            Integer foo = new Integer(text);
            canvas.mySkipList.ClearTouchedFlag();
            canvas.mySkipList.Remove(foo.intValue());
            canvas.repaint();
        } else if ("search".equals(e.getActionCommand())) {
            String text = keyField.getText();
            Integer foo = new Integer(text);
            canvas.mySkipList.ClearTouchedFlag();
            canvas.mySkipList.Find(foo.intValue());
            canvas.repaint();
        } else if ("clear_path".equals(e.getActionCommand())) {
            canvas.mySkipList.ClearTouchedFlag();
            canvas.repaint();
        }
    }
}
