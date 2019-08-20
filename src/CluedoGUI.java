import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * The CluedoGUI is responsible for managing the GUI and it technically the front end of the game
 *
 * @author Joshua Richards 300402562 | Melina Ariyani 300407485
 */
public class CluedoGUI extends JFrame implements KeyListener {
    private JPanel panel;
    private JButton suggestionButton;
    private JButton assumptionButton;
    private JButton exitButton;


    private ArrayList<Board.Cards> solution = new ArrayList<>(); //list of the solution to the game
    private boolean foundSolution = false;
    private static final int END_TURN = 12;
    private int numPlayers;
    private int keyPressed;
    private Board b;
    private Player p;
    private int numMovesLeft;

    public CluedoGUI() {
        setNumPlayers();
        add(panel);
        setTitle("Cluedo Game");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        suggestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suggestionAction(b, p);
                numMovesLeft = numMovesLeft-END_TURN;
            }
        });
        assumptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accusationAction(b, p);
                numMovesLeft = numMovesLeft-END_TURN;
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitAction(b, p);
                numMovesLeft--;
            }
        });

        try {
            setSolution(); // randomly sets solution for the game
            b = new Board(numPlayers, solution, "board.txt"); // creates a new board

            /*--------------------------------*/
            /*         MAIN GAME LOOP         */
            /*--------------------------------*/

            for (int player = 0; player < numPlayers; player++) {
                p = b.getPlayers().get(player);
                if (p.isOutOfGame()) continue;
                b.printBoard();
                System.out.println();

                //Rolling dice
                int movesLeft = diceRoll(12, 2);
                JOptionPane.showMessageDialog(null, p.getName() + "'s " + "(Player " + (b.getPlayers().indexOf(p) + 1));
                numMovesLeft = movesLeft;
                //Iterates through every move that is left.
                for (int i = 0; i < movesLeft; i++) {
                    System.out.println("Your Choices:");
                    System.out.println();

                    //Player enters room
                    if (p.isInRoom())
                        System.out.println("((((((((You are in the " + p.getRoom().toString() + "))))))))");
                    if (p.isInRoom()) System.out.println("You can make a SUGGESTION, ACCUSATION or EXIT");
                    else System.out.println("You have " + numMovesLeft + " moves left.");

                    //Prompting player to make a choice
                    System.out.println("What do you want to do?");
                    int num = 1;//playerInput(b, p, s);

                    if (foundSolution) break;
                    if (p.isOutOfGame()) break;

                    //If player view options etc. ensures not to decrement moves left
                    if (num == 0) i = i - 1;

                    numMovesLeft = numMovesLeft - num;
                    if (numMovesLeft <= 0) {
                        i = +END_TURN;
                    }
                    if (num != 0) b.printBoard();
                }

                //Check to see if game has been won
                if (foundSolution) break;

                //Turn has ended, moving to next player
                JOptionPane.showMessageDialog(null, p.getName() + "'s turn has ended, Next players turn.");
                if (player == numPlayers - 1) player = -1;
            }

            /*--------------------------------*/
            /*            GAME OVER           */
            /*--------------------------------*/
            System.out.println("******** G A M E   O V E R ******** \n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     */
    public void setNumPlayers() {
        String numPlayersString = JOptionPane.showInputDialog("How many players? (3-6)");
        numPlayers = Integer.parseInt(numPlayersString);
        //Making sure there is a valid number of players
        if (numPlayers > 6 || numPlayers < 3) throw new IllegalArgumentException("You must enter a number between 3-6.");
    }


    /**
     *
     *
     * @return
     */
    public int leftAction() {
        Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
        if (b.isMoveValid("LEFT", p)) {
            Position newPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol() - 1];
            b.move(oldPos, newPos, p);
            return 1;
        } else {
            return 999;
        }
    }

    /**
     *
     *
     * @return
     */
    public int rightAction() {
        Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
        if (b.isMoveValid("RIGHT", p)) {
            Position newPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol() + 1];
            b.move(oldPos, newPos, p);
            return 1;
        } else {
            return 999;
        }
    }

    /**
     *
     *
     * @return
     */
    public int upAction() {
        Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
        if (b.isMoveValid("UP", p)) {
            Position newPos = b.getPositions()[p.getPosition().getRow() - 1][p.getPosition().getCol()];
            b.move(oldPos, newPos, p);
            return 1;
        } else {
            return 999;
        }
    }

    /**
     *
     *
     * @return
     */
    public int downAction() {
        Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
        if (b.isMoveValid("DOWN", p)) {
            Position newPos = b.getPositions()[p.getPosition().getRow() + 1][p.getPosition().getCol()];
            b.move(oldPos, newPos, p);
            return 1;
        } else {
            return 999;
        }
    }

    /**
     *
     *
     * @param board
     * @param player
     * @return
     */
    public int exitAction(Board board, Player player) {
        if (player.isInRoom()) {
            Position oldPos = board.getPositions()[player.getPosition().getRow()][player.getPosition().getCol()];
            if (board.isMoveValid("EXIT", player)) {
                Position newPos = player.getRoom().getDoors().get(0);
                System.out.println(oldPos);
                System.out.println(newPos);
                board.move(oldPos, newPos, player);
                return 1;
            } else {
                return 999;
            }
        } else {
            return 999;
        }
    }

    /**
     *
     *
     * @param board
     * @param player
     * @return
     */
    public int suggestionAction(Board board, Player player) {
        if (player.isInRoom()) {
            String suggestion = JOptionPane.showInputDialog("List the Player and Weapon you are suggesting were part of the murder with no spaces e.g. (mrswhite dagger)");
            String[] suggestionArray = suggestion.split(" ");
            String characterAccusation = suggestionArray[0];
            String roomAccusation = player.getRoom().toString().toUpperCase();
            String weaponAccusation = suggestionArray[1];
            boolean provenWrong = false;
            for (int i = 0; i < numPlayers; i++) {
                if (board.getPlayers().get(i).toString().equals(player.toString())) continue;
                ArrayList<Board.Cards> hand = board.getPlayers().get(i).getHand();
                for (int j = 0; j < hand.size(); j++) {
                    if (characterAccusation.equals(hand.get(j).toString().toUpperCase()) ||
                            roomAccusation.equals(hand.get(j).toString().toUpperCase()) ||
                            weaponAccusation.equals(hand.get(j).toString().toUpperCase())) {
                        JOptionPane.showMessageDialog(null, hand.get(j).toString() + " card is in the hands of " + board.getPlayers().get(i).toString());
                        provenWrong = true;
                        i = j = 999;
                    }
                }
                if (!provenWrong)
                    JOptionPane.showMessageDialog(null, board.getPlayers().get(i).toString() + " doesn't have any of these cards");
            }
            if (!provenWrong) JOptionPane.showMessageDialog(null, "No players have cards of your suggestion");
            System.out.println();
            return END_TURN;
        } else {
            return 999;
        }
    }

    /**
     *
     *
     * @param board
     * @param player
     * @return
     */
    public int accusationAction(Board board, Player player) {
        if (player.isInRoom()) {
            String suggestion = JOptionPane.showInputDialog("List the Player, Weapon and Room you are Accusing were part of the murder with no spaces in the names e.g. (mrswhite diningroom dagger)");
            String[] suggestionArray = suggestion.split(" ");
            String characterAccusation = suggestionArray[0];
            String roomAccusation = suggestionArray[1];
            String weaponAccusation = suggestionArray[2];
            if (characterAccusation.equals(solution.get(0).name().toUpperCase()) && // if character is correct
                    roomAccusation.equals(solution.get(1).name().toUpperCase()) && // if room is correct
                    weaponAccusation.equals(solution.get(2).name().toUpperCase())) { // if weapon is correct
                foundSolution = true;
                JOptionPane.showMessageDialog(null, player.getName() + " was correct!");
            } else {
                player.setIsOutOfGame(true);
                JOptionPane.showMessageDialog(null, "Unfortunately your accusation was incorrect and you (" + player.getName() + ") are now out of the game");
            }
            return END_TURN;
        } else {
            return 999;
        }
    }

    /**
     * Is a simulation of 2 dice being rolled
     *
     * @return int representing the output of 2 die of which is how many moves the player can make
     */
    private int diceRoll(int high, int low) {
        return (int) ((Math.random()*((high+1)-low)) + low); // equivalent of rolling 2 die
    }

    /**
     * sets the solution randomly from the cards defined in the board class
     */
    private void setSolution() {
        solution.add(Board.Cards.values()[(int)(Math.random()*6)]); // Character first
        solution.add(Board.Cards.values()[(int)((Math.random()*9)+6)]); // Room second
        solution.add(Board.Cards.values()[(int)((Math.random()*6)+15)]); // Weapon Last
    }

    /**
     *
     *
     * @param event
     */
    @Override
    public void keyTyped(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_LEFT) { // if player pressed the left arrow key
            leftAction();
            numMovesLeft--;
        } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) { // if player pressed the right arrow key
            rightAction();
            numMovesLeft--;
        } else if (event.getKeyCode() == KeyEvent.VK_UP) { // if player pressed the up arrow key
            upAction();
            numMovesLeft--;
        } else if (event.getKeyCode() == KeyEvent.VK_DOWN) { // if player pressed the down arrow key
            downAction();
            numMovesLeft--;
        } else {
        }
    }

    @Override public void keyPressed(KeyEvent e) { }
    @Override public void keyReleased(KeyEvent e) { }

    /**
     * main class to the program
     * where to execute the code
     */
    public static void main(String[] arg) {
        CluedoGUI gui = new CluedoGUI();
        gui.setVisible(true);
    }
}
