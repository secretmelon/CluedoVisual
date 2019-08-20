import javax.swing.*;
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
    private JFrame frame;
    private JPanel panel;
    private JButton suggestionButton;
    private JButton assumptionButton;
    private JButton exitButton;
    private int input;

    private ArrayList<String> playerTurnChoices = new ArrayList<>(); // all possible player  inputs
    private ArrayList<Board.Cards> solution = new ArrayList<>(); //list of the solution to the game
    private boolean foundSolution = false;
    private static final int END_TURN = 12;
    private int numPlayers;
    private int keyPressed;

    public CluedoGUI() {
        setSolution();
        add(panel);
        setTitle("Cluedo Game");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void placeHolderForShitInConstructor(){
        try {
            setNumPlayers(); // finds out the number of players
            Board b = new Board(numPlayers, solution, "board.txt"); // creates a new board

            /*--------------------------------*/
            /*         MAIN GAME LOOP         */
            /*--------------------------------*/

            for (int player = 0; player < numPlayers; player++) {
                Player p = b.getPlayers().get(player);
                if (p.isOutOfGame()) continue;
                b.printBoard();
                System.out.println();

                //Rolling dice
                int movesLeft = diceRoll(12, 2);

                System.out.println("----------" + p.getName() + "'s " + "(Player " + (b.getPlayers().indexOf(p) + 1) + ") turn----------");

                int numLeft = movesLeft;

                //Iterates through every move that is left.
                for (int i = 0; i < movesLeft; i++) {
                    System.out.println("Your Choices:");
                    System.out.println();

                    //Player enters room
                    if (p.isInRoom())
                        System.out.println("((((((((You are in the " + p.getRoom().toString() + "))))))))");
                    if (p.isInRoom()) System.out.println("You can make a SUGGESTION, ACCUSATION or EXIT");
                    else System.out.println("You have " + numLeft + " moves left.");

                    //Prompting player to make a choice
                    System.out.println("What do you want to do?");
                    int num = 1;//playerInput(b, p, s);

                    if (foundSolution) break;
                    if (p.isOutOfGame()) break;

                    //If player view options etc. ensures not to decrement moves left
                    if (num == 0) i = i - 1;

                    //If player has an invalid move, ensures same as above, with message.
                    if (num == 999) {
                        System.out.println("Woops- this is an invalid move. Please choose a different option. \n \n");
                        i = i - 1;
                        num = 0;
                    }

                    numLeft = numLeft - num;
                    if (numLeft <= 0) {
                        i = +END_TURN;
                    }
                    if (num != 0) b.printBoard();
                }

                //Check to see if game has been won
                if (foundSolution) break;

                //Turn has ended, moving to next player
                System.out.println(p.getName() + "'s turn has ended, Next players turn.-------------------------------------------------------------------------------");
                System.out.println("-----------------------------------------------------------------------------------------------------------------------");
                System.out.println("-----------------------------------------------------------------------------------------------------------------------");
                System.out.println("-----------------------------------------------------------------------------------------------------------------------");
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

    public void setNumPlayers() {
        String numPlayersString = JOptionPane.showInputDialog("How many players? (3-6)");
        numPlayers = Integer.parseInt(numPlayersString);
        //Making sure there is a valid number of players
        if (numPlayers > 6 || numPlayers < 3) throw new IllegalArgumentException("You must enter a number between 3-6.");
    }

    /**
     * Gets the input of the player and does the relevant processing based on what it is
     *
     * @param b - board we are working with
     * @param p - player that is inputting the command
     * @param s - scanner that we are reading the players inputs from
     * @return int - how it will affect the remaining moves the player has
     * @throws InterruptedException - if the waiting mechanic fails
     */
    private int playerInput(Board b, Player p, Scanner s) throws InterruptedException {
        String input = s.next();

        //Allows the word or the first letter of word for quick game play
        //Moving left by one cell
        if (input.equalsIgnoreCase("LEFT") || input.equalsIgnoreCase("L")) {
            Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
            if (b.isMoveValid(input, p)) {
                Position newPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol() - 1];
                b.move(oldPos, newPos, p);
                return 1;
            } else {
                return 999;
            }
        }

        //Moving right by one cell
        else if (input.equalsIgnoreCase("RIGHT") || input.equalsIgnoreCase("R")) {
            Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
            if (b.isMoveValid(input, p)) {
                Position newPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol() + 1];
                b.move(oldPos, newPos, p);
                return 1;
            } else {
                return 999;
            }
        }

        //Moving up by one cell
        else if (input.equalsIgnoreCase("UP") || input.equalsIgnoreCase("U")) {
            Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
            if (b.isMoveValid(input, p)) {
                Position newPos = b.getPositions()[p.getPosition().getRow() - 1][p.getPosition().getCol()];
                b.move(oldPos, newPos, p);
                return 1;
            } else {
                return 999;
            }
        }

        //Moving down by one cell
        else if (input.equalsIgnoreCase("DOWN") || input.equalsIgnoreCase("D")) {
            Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
            if (b.isMoveValid(input, p)) {
                Position newPos = b.getPositions()[p.getPosition().getRow() + 1][p.getPosition().getCol()];
                b.move(oldPos, newPos, p);
                return 1;
            } else {
                return 999;
            }
        }

        //Making an accusation
        else if (input.equalsIgnoreCase("ACCUSATION")) {
            if (p.isInRoom()) {
                System.out.println("Are you sure you want to make an accusation? If you choose incorrectly you will be out of the game! (Y/N)");
                String choice = s.next();
                if (choice.equalsIgnoreCase("n")) return 999;
                System.out.println("Who do you think the murderer is, the room it was committed in and with what weapons?");
                System.out.println("Use the below format and write names without and spaces (spelling is important):");
                System.out.println("Character Room Weapon");
                String characterAccusation = s.next().toUpperCase();
                String roomAccusation = s.next().toUpperCase();
                String weaponAccusation = s.next().toUpperCase();
                if (characterAccusation.equals(solution.get(0).name().toUpperCase()) && // if character is correct
                        roomAccusation.equals(solution.get(1).name().toUpperCase()) && // if room is correct
                        weaponAccusation.equals(solution.get(2).name().toUpperCase())) { // if weapon is correct
                    foundSolution = true;
                    System.out.println(p.getName() + " was correct!");
                } else {
                    p.setIsOutOfGame(true);
                    System.out.println("Unfortunately your accusation was incorrect and you (" + p.getName() + ") are now out of the game\n\n");
                    TimeUnit.SECONDS.sleep(2);
                }
                return END_TURN;
            } else {
                return 999;
            }
        }

        //Making a suggestion
        else if (input.equalsIgnoreCase("SUGGESTION")) {
            if (p.isInRoom()) {
                System.out.println("Who are you suggesting the murderer is and with what weapon? (as the suggested room must be the one you are currently in)");
                System.out.println("Use the below format and write names without and spaces (spelling is important):");
                System.out.println("Character Weapon");
                String characterAccusation = s.next().toUpperCase();
                String roomAccusation = p.getRoom().toString().toUpperCase();
                String weaponAccusation = s.next().toUpperCase();
                boolean provenWrong = false;
                for (int i = 0; i < numPlayers; i++) {
                    if (b.getPlayers().get(i).toString().equals(p.toString())) continue;
                    ArrayList<Board.Cards> hand = b.getPlayers().get(i).getHand();
                    for (int j = 0; j < hand.size(); j++) {
                        if (characterAccusation.equals(hand.get(j).toString().toUpperCase()) ||
                                roomAccusation.equals(hand.get(j).toString().toUpperCase()) ||
                                weaponAccusation.equals(hand.get(j).toString().toUpperCase())) {
                            System.out.println(hand.get(j).toString() + " card is in the hands of " + b.getPlayers().get(i).toString());
                            provenWrong = true;
                            i = j = 999;
                        }
                    }
                    if (!provenWrong)
                        System.out.println(b.getPlayers().get(i).toString() + " doesn't have any of these cards");
                    TimeUnit.SECONDS.sleep(2);
                }
                if (!provenWrong) System.out.println("No players have cards of your suggestion");
                System.out.println();
                TimeUnit.SECONDS.sleep(2);
                return END_TURN;
            } else {
                return 999;
            }

        }

        //Exit the room
        else if (input.equalsIgnoreCase("EXIT")) {
            if (p.isInRoom()) {
                Position oldPos = b.getPositions()[p.getPosition().getRow()][p.getPosition().getCol()];
                if (b.isMoveValid(input, p)) {
                    Position newPos = p.getRoom().getDoors().get(0);
                    System.out.println(oldPos);
                    System.out.println(newPos);
                    b.move(oldPos, newPos, p);
                    return 1;
                } else {
                    return 999;
                }
            }
        }
        return 999;
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


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        input = e.getKeyCode();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * main class to the program
     * where to execute the code
     */
    public static void main(String[] arg) {
        CluedoGUI gui = new CluedoGUI();
        gui.setVisible(true);
    }
}
