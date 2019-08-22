import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
    private int numFailedPlayer = 0;
    private Board b;
    private Board backBoard;
    private Player p;
    private int numMovesLeft;
    private boolean board = false;
    private int currentPlayer;

    /**
     * constructor that initialises the GUI, number of players, solution and then runs the game loop
     *
     * @throws IOException
     */
    private CluedoVisualGame() throws IOException {
        super();
        setNumPlayers(); // sets the number of the players in the game
        setSolution(); // randomly sets solution for the game
        gameLoop();
    }

    /**
     * the main game loop, loops through the players then through the number of moves for each player
     */
    private void gameLoop() {
        try {
            backBoard = new Board(numPlayers, solution, "backboard.txt"); // creates the back board
            b = new Board(numPlayers, solution, "board.txt"); // creates a new board
            board = true;

            /*--------------------------------
                     MAIN GAME LOOP
            --------------------------------*/

            for (int player = 0; player < numPlayers; player++) {
                currentPlayer = player;
                p = b.getPlayers().get(player);
                if (p.isOutOfGame()) continue;
                if (numFailedPlayer == numPlayers-1) break;

                //Rolling dice
                int movesLeft = diceRoll(12, 2);
                JOptionPane.showMessageDialog(null, p.getName() + "'s " + "(Player " + (b.getPlayers().indexOf(p) + 1) + ")");
                numMovesLeft = movesLeft;
                //Iterates through every move that is left.
                boolean bool = true;
                while(numMovesLeft > 0){
                    redraw();
                    //Player enters room
                    if (p.isInRoom() && bool) {
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
                System.out.println(numPlayers);


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

    /**
     * gets the string of the item that your clicking on
     *
     * @param point - location of the click
     * @return - name of the item
     */
    @Override
    protected String getItemName(Point point) {
        int row = point.y / 23;
        int col = point.x / 23;
        if (b.getPositions()[row][col].getItem() != null) {
            return b.getPositions()[row][col].getItem().toString();
        }
        for (int i = 0; i < b.getRooms().size(); i++) {
            if (b.getPositions()[row][col].getCharValue() == b.getRooms().get(i).getLetter().toString().charAt(0)) {
                return b.getRooms().get(i).toString();
            }
        }
        return "";
    }

    /**
     * draws the entire graphics pane including board, cards and the number of moves
     * you have left
     *
     * @param g - graphics panel
     * @throws IOException
     */
    @Override
    protected void redraw(Graphics g) throws IOException {
        g.setColor(Color.darkGray);
        g.fillRect(0,0,9999,9999);
        if (!board)return;
        backBoard.printBoard(g);
        b.printBoard(g);
        //add in dice pic
        g.setColor(Color.cyan);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 35));
        g.drawString(Integer.toString(numMovesLeft), 85, (25*23)+65);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        g.drawString(p.toString()+"'s turn", 420, 18);
        BufferedImage MovesLeft = ImageIO.read(new File("Images\\MovesLeft.png"));
        g.drawImage(MovesLeft, 10, (25*23)+30, null);
        printCards(g);
    }

    /**
     * draws the cards to the graphics pane
     *
     * @param g - graphics panel
     * @throws IOException - in case the file isn't there
     */
    private void printCards(Graphics g) throws IOException {
        ArrayList<Board.Cards> hand = p.getHand();
        for (int i = 0; i < p.getHand().size(); i++) {
            BufferedImage card = ImageIO.read(new File("Images\\Cards\\" + p.getHand().get(i) + ".png"));
            g.drawImage(card, (130+((70*i) + 10)), (25*23)+10, null);
        }

    }

    /**
     * based on what button you press it will take you to the relevant method move either
     * move ur character or another option
     *
     * @param move - string representing a move
     */
    @Override
    protected void onMove(String move) {
        if (move.equals("SUGGESTION")) {
            suggestionAction();
        } else if (move.equals("ACCUSATION")) {
            accusationAction();
        } else if (move.equals("EXIT")) {
            exitAction();
            if(p.isInRoom())numMovesLeft--;
        } else if (move.equals("END")) {
            if (p.isInRoom()) numMovesLeft = 0; // can only end turn if they are in a room
        } else if (move.equals("LEFT")) {
            leftAction();
            numMovesLeft--;
        } else if (move.equals("RIGHT")) {
            rightAction();
            numMovesLeft--;
        } else if (move.equals("UP")) {
            upAction();
            numMovesLeft--;
        } else if (move.equals("DOWN")) {
            downAction();
            numMovesLeft--;
        }
    }

    /**
     * creates a pop up to ask the player how many players there will be for this game
     */
    private void setNumPlayers() {
        String numPlayersString = JOptionPane.showInputDialog("How many players? (3-6)");
        numPlayers = Integer.parseInt(numPlayersString);
        //Making sure there is a valid number of players
        if (numPlayers > 6 || numPlayers < 3) throw new IllegalArgumentException("You must enter a number between 3-6.");
    }


    /**
     * moves the player left if it is a valid move
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
     * moves the player right if its a valid move
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
     * moves the player up if its a valid move
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
     * moves the player down if its a valid move
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
     * exits the player out of a room it its a valid move
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
     * lets the player make a suggestion on what the murder weapon and
     * character is if they are aloud to do so
     */
    private void suggestionAction() {
        if (p.isInRoom()) {
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
     * lets the player make an accusation on what the murder weapon, room and
     * character is if they are aloud to do so
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
            numFailedPlayer++;
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
