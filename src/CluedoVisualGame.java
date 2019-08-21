import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
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
    private Board backBoard;
    private Player p;
    private int numMovesLeft;


    public CluedoVisualGame() throws IOException {
        setNumPlayers(); // sets the number of the players in the game
        setSolution(); // randomly sets solution for the game
        System.out.println(solution);
        gameLoop();
    }

    private boolean board = false;

    public void gameLoop() {
        try {
            backBoard = new Board(numPlayers, solution, "backboard.txt"); // creates the back board
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
                System.out.println(movesLeft);
                //Iterates through every move that is left.
                boolean bool = true;
                while(numMovesLeft > 0){
                    redraw();
                    //Player enters room
                    //System.out.println(numMovesLeft);
                    if (p.isInRoom() && bool) {
                        System.out.println(p.getRoom());
                        JOptionPane.showMessageDialog(null, "You are in the " + p.getRoom().toString());
                        bool = false;
                    }
                    if (foundSolution) {
                        numPlayers--;
                        break;
                    }
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
    protected void redraw(Graphics g) throws IOException {
        g.setColor(Color.darkGray);
        g.fillRect(0,0,9999,9999);
        if (!board)return;
        backBoard.printBoard(g);
        b.printBoard(g);
        //add in dice pic
        g.setColor(Color.cyan);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString(Integer.toString(numMovesLeft), 100, (25*32)+65);
        printCards(g);
    }

    private void printCards(Graphics g) throws IOException {
        ArrayList<Board.Cards> hand = p.getHand();
        for (int i = 0; i < p.getHand().size(); i++) {
            BufferedImage card = ImageIO.read(new File("Images\\Cards\\" + p.getHand().get(i) + ".png"));
            g.drawImage(card, (200+((70*i) + 10)), (25*32)+10, null);
        }

    }

    @Override
    protected void onMove(String move) {
        if (move.equals("SUGGESTION")) {
            suggestionAction();
        }
        else if (move.equals("ACCUSATION")) {
            accusationAction();
        }
        else if (move.equals("EXIT")) {
            exitAction();
            numMovesLeft--;
            System.out.println(numMovesLeft);
        } else if (move.equals("END")) {
            numMovesLeft = 0;
        } else if (move.equals("LEFT")) {
            leftAction();
            numMovesLeft--;
            System.out.println(numMovesLeft);
        }
        else if (move.equals("RIGHT")) {
            rightAction();
            numMovesLeft--;
            System.out.println(numMovesLeft);
        }
        else if (move.equals("UP")) {
            upAction();
            numMovesLeft--;
            System.out.println(numMovesLeft);
        }
        else if (move.equals("DOWN")) {
            downAction();
            numMovesLeft--;
            System.out.println(numMovesLeft);
        }
    }

    /**
     *
     *
     */
    private void setNumPlayers() {
        String numPlayersString = JOptionPane.showInputDialog("How many players? (3-6)");
        numPlayers = Integer.parseInt(numPlayersString);
        //Making sure there is a valid number of players
        if (numPlayers > 6 || numPlayers < 3) throw new IllegalArgumentException("You must enter a number between 3-6.");
    }


    /**
     *
     *
     *
     */
    private void leftAction() {
        Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
        if (b.isMoveValid("LEFT", p)) {
            Position newPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol() - 1];
            b.move(oldPos, newPos, p);
        } else {
            numMovesLeft++;
        }
    }

    /**
     *
     *
     *
     */
    private void rightAction() {
        Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
        if (b.isMoveValid("RIGHT", p)) {
            Position newPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol() + 1];
            b.move(oldPos, newPos, p);
        } else {
            numMovesLeft++;
        }
    }

    /**
     *
     *
     *
     */
    private void upAction() {
        Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
        if (b.isMoveValid("UP", p)) {
            Position newPos = b.getPositions()[p.getPosition().getRow() - 1][p.getPosition().getCol()];
            b.move(oldPos, newPos, p);
        } else {
            numMovesLeft++;
        }
    }

    /**
     *
     *
     *
     */
    private void downAction() {
        Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
        if (b.isMoveValid("DOWN", p)) {
            Position newPos = b.getPositions()[p.getPosition().getRow() + 1][p.getPosition().getCol()];
            b.move(oldPos, newPos, p);
        } else {
            numMovesLeft++;
        }
    }

    /**
     *
     *
     *
     */
    private void exitAction() {
        if (p.isInRoom()) {
            Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
            if (b.isMoveValid("EXIT", p)) {
                Position newPos = p.getRoom().getDoors().get(0);
                b.move(oldPos, newPos, p);
            } else {
                numMovesLeft++;
            }
        }
    }

    /**
     *
     *
     *
     */
    private void suggestionAction() {
        if (p.isInRoom()) {
            System.out.println(p.getHand());
            String characterAccusation = JOptionPane.showInputDialog("What character are you suggesting committed the murder? (no spaces e.g. mrswhite)").toUpperCase();
            String weaponAccusation = JOptionPane.showInputDialog("And with what weapon are you suggesting the murder was committed with? (no spaces e.g. leadpipe)").toUpperCase();
            String roomAccusation = p.getRoom().toString().toUpperCase();
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
            numMovesLeft = 0;
        }
    }

    /**
     *
     *
     *
     */
    private void accusationAction() {
        String characterAccusation = JOptionPane.showInputDialog("What character are you accusing? (no spaces e.g. mrswhite)").toUpperCase();
        String roomAccusation = JOptionPane.showInputDialog("Which room are you accusing? (no spaces e.g. diningroom)").toUpperCase();
        String weaponAccusation = JOptionPane.showInputDialog("And what weapon are you accusing? (no spaces e.g. leadpipe)").toUpperCase();
        if (characterAccusation.equals(solution.get(0).name().toUpperCase()) && // if character is correct
                roomAccusation.equals(solution.get(1).name().toUpperCase()) && // if room is correct
                weaponAccusation.equals(solution.get(2).name().toUpperCase())) { // if weapon is correct
            foundSolution = true;
            JOptionPane.showMessageDialog(null, p.getName() + " was correct!");
        } else {
            p.setIsOutOfGame(true);
            JOptionPane.showMessageDialog(null, "Unfortunately your accusation was incorrect and you (" + p.getName() + ") are now out of the game");
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
