import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * a player in the game of cluedo
 *
 * @author Joshua Richards 300402562 | Melina Arianyi
 */
public class Player implements Item{

    /**
     * the different possible names of the characters
     */
    enum CharacterName {MrsWhite, MrGreen, MrsPeacock, ColMustard, ProfPlum, MissScarlett}

    private ArrayList<Board.Cards> hand = new ArrayList<>(); // list representing the hand of the player
    private Room room; // the room that the player is in (null if in the hallways)
    private CharacterName name; // players character name
    private Position pos; // players current position
    private boolean outOfGame = false; // if the player has made an accusation and failed to guess correctly
    private boolean inRoom = false; // if the player is in a room or not
    private int playerNumber = 0;

    /**
     * creates a new player object
     *
     * @param name - CharacterName of the player
     * @param pos - Players starting position
     */
    public Player(CharacterName name, Position pos) {
        this.name = name;
        this.pos = pos;

        if (name.toString().equals("MissScarlett")) {
            playerNumber = 1;
        } else if (name.toString().equals("ProfPlum")) {
            playerNumber = 2;
        } else if (name.toString().equals("ColMustard")) {
            playerNumber = 3;
        } else if (name.toString().equals("MrsPeacock")) {
            playerNumber = 4;
        } else if (name.toString().equals("MrGreen")) {
            playerNumber = 5;
        } else if (name.toString().equals("MrsWhite")) {
            playerNumber = 6;
        }
    }

    /**
     * gets the name of the player
     *
     * @return CharacterName of the player
     */
    public CharacterName getName() {
        return name;
    }

    /**
     * gets the players hand
     *
     * @return - list representing the players hand
     */
    public ArrayList<Board.Cards> getHand() {
        return hand;
    }

    /**
     * adds a card to the players hand
     *
     * @param card - card to be added to the hand
     */
    public void addToHand(Board.Cards card) {
        this.hand.add(card);
    }

    /**
     * gets the room that the player is in (null if in the hallways)
     *
     * @return room that the player is in
     */
    @Override public Room getRoom() {
        return room;
    }

    /**
     * sets the room that the player will be in
     *
     * @param room - room that the player will be in
     */
    @Override public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * gets the players current position
     *
     * @return position of the player
     */
    @Override public Position getPosition() {
        return pos;
    }

    /**
     * sets the position that the player will be in
     *
     * @param pos - position to be set as the position of the player
     */
    @Override public void setPosition(Position pos) {
        this.pos = pos;
    }

    /**
     * if the player is out of the game (made an incorrect accusation)
     *
     * @return boolean if they are in or out of the game
     */
    public boolean isOutOfGame() {
        return outOfGame;
    }

    /**
     * sets whether the player is in or out of the game (made an incorrect accusation or not)
     *
     * @param outOfGame boolean if the player is in or out of the game
     */
    public void setIsOutOfGame(boolean outOfGame) {
        this.outOfGame = outOfGame;
    }

    /**
     * if the player is in a room or not
     *
     * @return boolean if the player is in a room or not
     */
    public boolean isInRoom() {
        return inRoom;
    }

    /**
     * sets the boolean if the player is in a room or not
     *
     * @param inRoom - boolean if the player is in a room or not
     */
    @Override public void setInRoom(boolean inRoom) {
        this.inRoom = inRoom;
    }

    /**
     * character representation of the player
     *
     * @return char that is the players number
     */
    @Override public char getChar() {
        return (char)('0' + getPlayerNumber());
    }

    /**
     * returns the number that corresponds to the player on the board
     *
     * @return int that is the players number
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * string form of the players name
     *
     * @return - string representing the players name
     */
    @Override public String toString() {
        if (name.equals("null")) return "";
        return name.toString();
    }
}
