import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.Border;

/**
 *
 * The GUI of the program made using swing
 *
 * abstract class of which I extend in the CluedoVisualGame class
 *
 * to make this GUI I briefly referenced the
 * COMP261 Assignment 1 - Auckland Road Maps
 *
 * @author @author Joshua Richards 300402562 | Melina Ariyani 300407485
 */
public abstract class GUI implements KeyListener{

    /**
     * returns the name of the item that the user is clicking on
     *
     * @param point - place that the user is clicking
     * @return - the name of the item
     */
    protected abstract String getItemName(Point point);

    /**
     * Is called when the drawing area is redrawn and performs all the logic for
     * the actual drawing, which is done with the passed Graphics object.
     *
     * @param g - graphics panel
     */
    protected abstract void redraw(Graphics g) throws IOException;

    /**
     * Is called whenever a movement button is pressed. a capitalised string of the
     * move direction, representing the button clicked by the user.
     *
     * @param move - string representing the move
     */
    protected abstract void onMove(String move);

    /**
     * Redraws the graphics window.
     */
    public void redraw() {
        frame.repaint();
    }

    private static final int DEFAULT_DRAWING_HEIGHT = 680; // graphics panel height
    private static final int DEFAULT_DRAWING_WIDTH = 600; // graphics panel width
    private JFrame frame;

    /**
     * constructor that just calls the initialise method
     */
    public GUI() {
        initialise();
    }

    /**
     * where all of the swing components are created
     */
    private void initialise() {
        JPanel controls;
        JComponent drawing; // we customise this to make it a drawing pane.
         // first, we make the buttons etc. that go along the top bar and also the menu bar.

        // Creates a text box for inputs if the user prefers to type in there actions
        JTextField jt = new JTextField(10);
        jt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onMove(jt.getText().toUpperCase());
                jt.setText("");
                redraw();
            }
        });
        JLabel textFieldLabel = new JLabel("Action:");

        // Creating the Menu Bar
        JMenuBar mb = new JMenuBar();
        JMenu x = new JMenu("Options");
        JMenuItem quit = new JMenuItem("Quit Game");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                System.exit(0); // cleanly end the program.
            }
        });
        x.add(quit);
        mb.add(x);

        // Creating the Accusation button
        JButton Accusation = new JButton("Accusation");
        Accusation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                onMove("ACCUSATION");
                redraw();
            }
        });

        // Creating the Suggestion button
        JButton Suggestion = new JButton("Suggestion");
        Suggestion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                onMove("SUGGESTION");
                redraw();
            }
        });

        // Creating the Exit Room
        JButton ExitRoom = new JButton("Exit Room");
        ExitRoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                onMove("EXIT");
                redraw();
            }
        });

        // Creating the End Turn button
        JButton EndTurn = new JButton("End Turn");
        EndTurn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                onMove("END");
                redraw();
            }
        });

        // Creating the Left move button
        JButton west = new JButton("\u2190");
        west.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                onMove("LEFT");
                redraw();
            }
        });

        // Creating the Right move button
        JButton east = new JButton("\u2192");
        east.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                onMove("RIGHT");
                redraw();
            }
        });

        // Creating the Up move button
        JButton north = new JButton("\u2191");
        north.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                onMove("UP");
                redraw();
            }
        });

        // Creating the Down move button
        JButton south = new JButton("\u2193");
        south.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                onMove("DOWN");
                redraw();
            }
        });





        // Next, make the top bar itself and arrange everything inside of it.
        controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));

        // make an empty border so the components aren't right up against the frame edge.
        Border edge = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        controls.setBorder(edge);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));
        // manually set a fixed size for the panel containing the Accusation and quit
        // buttons (doesn't change with window resize).
        buttonPanel.setMaximumSize(new Dimension(100, 100));
        buttonPanel.add(ExitRoom);
        buttonPanel.add(EndTurn);
        controls.add(buttonPanel);
        // rigid areas are invisible components that can be used to space
        // components out.
        controls.add(Box.createRigidArea(new Dimension(15, 0)));

        // movement arrows, accusation and suggestion buttons
        JPanel optionsPanel = new JPanel();
        optionsPanel.setMaximumSize(new Dimension(300, 60));
        optionsPanel.setLayout(new GridLayout(2, 3));
        optionsPanel.add(Suggestion);
        optionsPanel.add(north);
        optionsPanel.add(Accusation);
        optionsPanel.add(west);
        optionsPanel.add(south);
        optionsPanel.add(east);

        // creates the text box and also what item you click on will be displayed here
        JPanel textFieldPanel = new JPanel();
        textFieldLabel.setLayout(new GridLayout(2, 1));
        textFieldPanel.add(textFieldLabel, BorderLayout.NORTH);
        textFieldPanel.add(jt);

        controls.add(optionsPanel);
        controls.add(Box.createRigidArea(new Dimension(15, 0)));


        // clicking output panel
        JPanel clickingOutput = new JPanel();
        JLabel clickingLabel = new JLabel();

         // making the drawing canvas
        drawing = new JComponent() {
            protected void paintComponent(Graphics g) {
                try {
                    redraw(g);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        // listens to the location that the mouse is clicked at
        drawing.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                clickingLabel.setText(getItemName(e.getPoint()));
            }
        });
        drawing.setPreferredSize(new Dimension(DEFAULT_DRAWING_WIDTH,
                DEFAULT_DRAWING_HEIGHT));
        drawing.setVisible(true);

        // adding the clicked on item to the textFieldPanel
        clickingOutput.add(clickingLabel);
        textFieldPanel.add(clickingOutput);

        controls.add(textFieldPanel);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setDividerSize(5); // make the selectable area smaller
        split.setContinuousLayout(true); // make the panes resize nicely
        split.setResizeWeight(1); // always give extra space to drawings
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setTopComponent(drawing);

        frame = new JFrame("Cluedo Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.addKeyListener(this);
        frame.add(controls, BorderLayout.NORTH);
        frame.add(split, BorderLayout.CENTER);
        frame.setJMenuBar(mb);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * what actions happen when an arrow key is pressed
     *
     * @param e - the key that is pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("in key pressed");
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                onMove("UP");
                redraw();
                break;
            case KeyEvent.VK_DOWN:
                onMove("DOWN");
                redraw();
                break;
            case KeyEvent.VK_LEFT:
                onMove("LEFT");
                redraw();
                break;
            case KeyEvent.VK_RIGHT :
                onMove("RIGHT");
                redraw();
                break;
        }
    }

    @Override public void keyTyped(KeyEvent e) { }
    @Override public void keyReleased(KeyEvent e) {

    }

}