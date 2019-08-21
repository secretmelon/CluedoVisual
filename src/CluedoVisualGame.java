import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The CluedoVisualGame is responsible for managing the GUI and it technically the front end of the game
 *
 * @author Joshua Richards 300402562 | Melina Ariyani 300407485
 */
public class CluedoVisualGame extends GUI {

    private ArrayList<Board.Cards> solution = new ArrayList<>(); //list of the solution to the game
    private boolean foundSolution = false;
    private static final int END_TURN = 12;
    private int numPlayers;
    private Board b;
    private Player p;
    private int numMovesLeft;



    public CluedoVisualGame() throws IOException {
        setNumPlayers(); // sets the number of the players in the game
        setSolution(); // randomly sets solution for the game
        gameLoop();
    }

    private boolean board = false;

    public void gameLoop() {
        try {
            b = new Board(numPlayers, solution, "board.txt"); // creates a new board
            board = true;
            /*--------------------------------
                     MAIN GAME LOOP
            --------------------------------*/

            for (int player = 0; player < numPlayers; player++) {
                p = b.getPlayers().get(player);
                if (p.isOutOfGame()) continue;
                System.out.println();

                //Rolling dice
                int movesLeft = diceRoll(12, 2);
                JOptionPane.showMessageDialog(null, p.getName() + "'s " + "(Player " + (b.getPlayers().indexOf(p) + 1) + ")");
                numMovesLeft = movesLeft;
                //Iterates through every move that is left.
                while(numMovesLeft > 0){
                    redraw();
                    //Player enters room
                    System.out.println(numMovesLeft);
                    if (p.isInRoom()) JOptionPane.showMessageDialog(null, "You are in the " + p.getRoom().toString());
                    if (foundSolution) break;
                    if (p.isOutOfGame()) break;
                }

                //Check to see if game has been won
                if (foundSolution) break;

                //Turn has ended, moving to next player
                JOptionPane.showMessageDialog(null, p.getName() + "'s turn has ended, Next players turn.");
                if (player == numPlayers - 1) player = -1;
            }

            /*--------------------------------
                        GAME OVER
            --------------------------------*/
            JOptionPane.showMessageDialog(null, "******** G A M E   O V E R ******** \n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void redraw(Graphics g) {
        if (!board)return;
        b.printBoard(g);
    }

    @Override
    protected void onMove(String move) {
        if (move.equals("SUGGESTION")) suggestionAction();
        else if (move.equals("ACCUSATION")) accusationAction();
        else if (move.equals("EXIT")) exitAction();
        else if (move.equals("LEFT")) leftAction();
        else if (move.equals("RIGHT")) rightAction();
        else if (move.equals("UP")) upAction();
        else if (move.equals("DOWN")) downAction();
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
     * @return
     */
    public int exitAction() {
        if (p.isInRoom()) {
            Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
            if (b.isMoveValid("EXIT", p)) {
                Position newPos = p.getRoom().getDoors().get(0);
                System.out.println(oldPos);
                System.out.println(newPos);
                b.move(oldPos, newPos, p);
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
     * @return
     */
    public int suggestionAction() {
        if (p.isInRoom()) {
            String suggestion = JOptionPane.showInputDialog("List the Player and Weapon you are suggesting were part of the murder with no spaces e.g. (mrswhite dagger)");
            String[] suggestionArray = suggestion.split(" ");
            String characterAccusation = suggestionArray[0];
            String roomAccusation = p.getRoom().toString().toUpperCase();
            String weaponAccusation = suggestionArray[1];
            boolean provenWrong = false;
            for (int i = 0; i < numPlayers; i++) {
                if (b.getPlayers().get(i).toString().equals(p.toString())) continue;
                ArrayList<Board.Cards> hand = b.getPlayers().get(i).getHand();
                for (int j = 0; j < hand.size(); j++) {
                    if (characterAccusation.equals(hand.get(j).toString().toUpperCase()) ||
                            roomAccusation.equals(hand.get(j).toString().toUpperCase()) ||
                            weaponAccusation.equals(hand.get(j).toString().toUpperCase())) {
                        JOptionPane.showMessageDialog(null, hand.get(j).toString() + " card is in the hands of " + b.getPlayers().get(i).toString());
                        provenWrong = true;
                        i = j = 999;
                    }
                }
                if (!provenWrong)
                    JOptionPane.showMessageDialog(null, b.getPlayers().get(i).toString() + " doesn't have any of these cards");
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
     * @return
     */
    public int accusationAction() {
        if (p.isInRoom()) {
            String suggestion = JOptionPane.showInputDialog("List the Player, Weapon and Room you are Accusing were part of the murder with no spaces in the names e.g. (mrswhite diningroom dagger)");
            String[] suggestionArray = suggestion.split(" ");
            String characterAccusation = suggestionArray[0];
            String roomAccusation = suggestionArray[1];
            String weaponAccusation = suggestionArray[2];
            if (characterAccusation.equals(solution.get(0).name().toUpperCase()) && // if character is correct
                    roomAccusation.equals(solution.get(1).name().toUpperCase()) && // if room is correct
                    weaponAccusation.equals(solution.get(2).name().toUpperCase())) { // if weapon is correct
                foundSolution = true;
                JOptionPane.showMessageDialog(null, p.getName() + " was correct!");
            } else {
                p.setIsOutOfGame(true);
                JOptionPane.showMessageDialog(null, "Unfortunately your accusation was incorrect and you (" + p.getName() + ") are now out of the game");
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
     * main class to the program
     * where to execute the code
     */
    public static void main(String[] arg) throws IOException {
        new CluedoVisualGame();
    }
}
