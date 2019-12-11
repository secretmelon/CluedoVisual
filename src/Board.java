import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * builds the board and creates the players, weapons and rooms and puts them in there respective locations
 * also contains the methods to move the items around the board
 *
 * @author Joshua Richards 300402562 | Melina Arianyi
 */
public class Board {

    private Position[][] positions;                         //Array of cells in the board
    private ArrayList<Player> players = new ArrayList<>();  //List of the players
    private ArrayList<Room> rooms = new ArrayList<>();      //All rooms
    private ArrayList<Weapon> weapons = new ArrayList<>();  //All weapons
    private ArrayList<Cards> allCards = new ArrayList<>();  //All 'cards'
    private static final int ROWS = 25;                     //Number of rows on the board
    private static final int COLUMNS = 24;                  //Number of columns on the board
    private static final int NUM_ROOMS = 9;                 //Number of rooms
    private static final int NUM_WEAPONS = 6;               //Number of weapons
    private int numPlayers; // the number of players

    private final BufferedImage BallroomIcon = ImageIO.read(new File("Images\\Ball.png"));
    private final BufferedImage BilliardIcon = ImageIO.read(new File("Images\\Billiard .png"));
    private final BufferedImage CandleStickIcon = ImageIO.read(new File("Images\\Candle Stick.png"));
    private final BufferedImage ConservatoryIcon = ImageIO.read(new File("Images\\Conservatory.png"));
    private final BufferedImage DaggerIcon = ImageIO.read(new File("Images\\Dagger.png"));
    private final BufferedImage MrGreenIcon = ImageIO.read(new File("Images\\Green.png"));
    private final BufferedImage HallIcon = ImageIO.read(new File("Images\\Hall.png"));
    private final BufferedImage KitchenIcon = ImageIO.read(new File("Images\\Kitchen Floor.png"));
    private final BufferedImage DiningRoomIcon = ImageIO.read(new File("Images\\Layer 2.png"));
    private final BufferedImage LeadpipeIcon = ImageIO.read(new File("Images\\Lead Pipe.png"));
    private final BufferedImage LibraryIcon = ImageIO.read(new File("Images\\Library.png"));
    private final BufferedImage MainFloorIcon = ImageIO.read(new File("Images\\Main Floor.png"));
    private final BufferedImage ColMustardIcon = ImageIO.read(new File("Images\\Mustard.png"));
    private final BufferedImage MrsPeacockIcon = ImageIO.read(new File("Images\\Peacock.png"));
    private final BufferedImage ProfPlumIcon = ImageIO.read(new File("Images\\Plum.png"));
    private final BufferedImage RevolverIcon = ImageIO.read(new File("Images\\Revolver.png"));
    private final BufferedImage RopeIcon = ImageIO.read(new File("Images\\Rope.png"));
    private final BufferedImage MissScarlettIcon = ImageIO.read(new File("Images\\Scarlett.png"));
    private final BufferedImage SpannerIcon = ImageIO.read(new File("Images\\Spanner.png"));
    private final BufferedImage StudyIcon = ImageIO.read(new File("Images\\Study.png"));
    private final BufferedImage WallIcon = ImageIO.read(new File("Images\\Wall.png"));
    private final BufferedImage MrsWhiteIcon = ImageIO.read(new File("Images\\White.png"));


    /**
     * All the cards in the game.
     */
    enum Cards {MrsWhite, MrGreen, MrsPeacock, ColMustard, ProfPlum, MissScarlett,                   // first 6 are characters
        Ballroom, Conservatory, BilliardRoom, Library, Study, Hall, Lounge, DiningRoom, Kitchen,      // second 9 are rooms
        Candlestick, Dagger, LeadPipe, Revolver, Rope, Spanner}                                      // last 6 are weapons

    /**
     * loads in board and creates players and also creates hand for each player
     *
     * @param numPlayers
     * @param fileName
     */
    public Board(int numPlayers, ArrayList<Cards> solution, String fileName) throws IOException {



        /*--------------------------------*/
        /*         CREATING BOARD         */
        /*--------------------------------*/
        positions = new Position[ROWS][COLUMNS];
        this.numPlayers = numPlayers;

        Scanner s = null;
        int allocatedCharacters = 0;
        makeAllCards();
        try {
            if (fileName.equals("backboard.txt")) {
                s = new Scanner(new FileReader(fileName));
                for (int i = 0; s.hasNextLine(); i++) {
                    char[] currentLine = s.nextLine().toCharArray();
                    for (int j = 0; j < currentLine.length; j++) {
                        positions[i][j] = new Position(i, j, currentLine[j], null);
                    }
                }
                return;
            }
            /*--------------------------------*/
            /*        CREATING PLAYERS        */
            /*--------------------------------*/
            s = new Scanner(new FileReader(fileName));
            for (int i = 0; s.hasNextLine(); i++) {
                char[] currentLine = s.nextLine().toCharArray();
                for (int j = 0; j < currentLine.length; j++) {
                    positions[i][j] = new Position(i, j, currentLine[j], null);
                    //allocating the characters
                    if (Character.isDigit(positions[i][j].getCharValue())) {// if the character is a digit
                        Player player = new Player(Player.CharacterName.values()[allocatedCharacters], positions[i][j]);
                        positions[i][j].setItem(player);
                        addPlayer(player);
                        allocatedCharacters++;
                    }
                }
            }
            Collections.reverse(players);



            /*--------------------------------*/
            /*          DEALING CARDS         */
            /*--------------------------------*/
            Collections.shuffle(allCards);
            int player = 0;
            for (int i = 0; i < allCards.size(); i++) {
                if (!allCards.get(i).toString().equals(solution.get(0).toString()) && !allCards.get(i).toString().equals(solution.get(1).toString()) && !allCards.get(i).toString().equals(solution.get(2).toString())) {
                    players.get(player).addToHand(allCards.get(i));
                    player = player + 1;
                    if (player == numPlayers) player = 0;
                }
            }



            /*--------------------------------*/
            /*         CREATING ROOMS         */
            /*--------------------------------*/
            for (int i = 0; i < NUM_ROOMS; i++) {
                rooms.add(new Room(Room.RoomLetter.values()[i]));
            }
            for (int i = 0; i<ROWS; i++) {
                for (int j = 0; j<COLUMNS; j++) {
                    char c = positions[i][j].getCharValue();
                    if (Character.isLowerCase(c)) {
                        String letter = Character.toString(positions[i][j].getCharValue());
                        for (Room r : rooms) {
                            String rLetter = r.getLetter().toString();
                            if (rLetter.equals(letter)) {
                                r.addPosition(positions[i][j]);
                            }
                        }

                    }
                }
            }


            /*--------------------------------*/
            /*          CREATING DOORS        */
            /*--------------------------------*/
            for (int i = 0; i < rooms.size(); i++) {
                for (int j = 0; j < rooms.get(i).getRoomPositions().size(); j++) {
                    Position p = rooms.get(i).getRoomPositions().get(j);
                    if (positions[p.getRow()+1][p.getCol()].getCharValue() == '-' || positions[p.getRow()-1][p.getCol()].getCharValue() == '-' ||
                            positions[p.getRow()][p.getCol()+1].getCharValue() == '-' || positions[p.getRow()][p.getCol()-1].getCharValue() == '-') {
                        rooms.get(i).addDoor(p);
                    }
                }
            }


            /*----------------------------------*/
            /* REMOVE DOORS FROM MAIN POSITIONS */
            /*----------------------------------*/
            Iterator<Room> roomIterator = rooms.iterator();
            while(roomIterator.hasNext()) {
                ArrayList<Position> positionArrayList = roomIterator.next().getRoomPositions();
                Iterator<Position> positionIterator = positionArrayList.iterator();
                while (positionIterator.hasNext()) {
                    Position p = positionIterator.next();
                    if (positions[p.getRow()+1][p.getCol()].getCharValue() == '-' || positions[p.getRow()-1][p.getCol()].getCharValue() == '-' ||
                            positions[p.getRow()][p.getCol()+1].getCharValue() == '-' || positions[p.getRow()][p.getCol()-1].getCharValue() == '-') {
                        positionIterator.remove();
                    }
                }
            }



            /*--------------------------------*/
            /*    PLACING WEAPONS IN ROOMS    */
            /*--------------------------------*/
            for (int i = 0; i < NUM_WEAPONS; i++) {
                weapons.add(new Weapon(Weapon.WeaponName.values()[i]));
            }
            Collections.shuffle(rooms);
            int count = 0;
            for (Weapon w : weapons) {
                rooms.get(count).getRoomPositions().get(0).setItem(w);
                rooms.get(count).getRoomPositions().get(0).setCharValue(w.getChar());
                w.setInRoom(true);
                w.setRoom(rooms.get(count));
                count++;
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
    }


    /**
     * prints out the board on the graphics panel
     *
     * @param g - graphics panel
     */
    public void printBoard(Graphics g) {
        for (int j = 0; j < ROWS; j++) {
            for (int i = 0; i < COLUMNS; i++) {
                char c = positions[j][i].getCharValue();

                if (c == '#') g.drawImage(WallIcon, i*23, j*23, null);
                else if (c == '-') g.drawImage(MainFloorIcon, i*23, j*23, null);
                else if (c == '1') g.drawImage(MissScarlettIcon, i*23, j*23, null);
                else if (c == '2') g.drawImage(ProfPlumIcon, i*23, j*23, null);
                else if (c == '3') g.drawImage(ColMustardIcon, i*23, j*23, null);
                else if (c == '4') g.drawImage(MrsPeacockIcon, i*23, j*23, null);
                else if (c == '5') g.drawImage(MrGreenIcon, i*23, j*23, null);
                else if (c == '6') g.drawImage(MrsWhiteIcon, i*23, j*23, null);

                else if (c == 'B') g.drawImage(WallIcon, i*23, j*23, null);
                else if (c == 'b') g.drawImage(BallroomIcon, i*23, j*23, null);
                else if (c == 'C') g.drawImage(WallIcon, i*23, j*23, null);
                else if (c == 'c') g.drawImage(ConservatoryIcon, i*23, j*23, null);
                else if (c == 'I') g.drawImage(WallIcon, i*23, j*23, null);
                else if (c == 'i') g.drawImage(BilliardIcon, i*23, j*23, null);
                else if (c == 'L') g.drawImage(WallIcon, i*23, j*23, null);
                else if (c == 'l') g.drawImage(LibraryIcon, i*23, j*23, null);
                else if (c == 'S') g.drawImage(WallIcon, i*23, j*23, null);
                else if (c == 's') g.drawImage(StudyIcon, i*23, j*23, null);
                else if (c == 'H') g.drawImage(WallIcon, i*23, j*23, null);
                else if (c == 'h') g.drawImage(HallIcon, i*23, j*23, null);
                else if (c == 'G') g.drawImage(WallIcon, i*23, j*23, null);
                else if (c == 'g') g.drawImage(StudyIcon, i*23, j*23, null);
                else if (c == 'D') g.drawImage(WallIcon, i*23, j*23, null);
                else if (c == 'd') g.drawImage(DiningRoomIcon, i*23, j*23, null);
                else if (c == 'K') g.drawImage(WallIcon, i*23, j*23, null);
                else if (c == 'k') g.drawImage(KitchenIcon, i*23, j*23, null);
                else if (c == 'k') g.drawImage(KitchenIcon, i*23, j*23, null);

                else if (c == '|') g.drawImage(CandleStickIcon, i*23, j*23, null);
                else if (c == '+') g.drawImage(DaggerIcon, i*23, j*23, null);
                else if (c == '!') g.drawImage(LeadpipeIcon, i*23, j*23, null);
                else if (c == '*') g.drawImage(RevolverIcon, i*23, j*23, null);
                else if (c == '%') g.drawImage(RopeIcon, i*23, j*23, null);
                else if (c == '^') g.drawImage(SpannerIcon, i*23, j*23, null);
                System.out.println("hi");
            }
        }
    }

    /**
     * prints out an ASCII representation of what the board currently looks like
     */
    public void printBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                System.out.print(positions[i][j].getCharValue() + " ");
            }
            System.out.print('\n');
        }
        System.out.println();
    }

    /**
     * creates the list of all cards (allCards)
     */
    public void makeAllCards() {
        for (int i = 0; i < Cards.values().length; i++) {
            allCards.add(Cards.values()[i]);
        }
    }

    /*--------------------------------*/
    /*           MOVE METHODS         */
    /*--------------------------------*/
    /**
     *
     * checks whether a given move is possible within the rules of the game
     *
     * @param move - what the move is
     * @param p - the player that is wanting to move
     * @return boolean whether you can make the move or not
     */
    public boolean isMoveValid(String move, Player p) {
        if (move.equalsIgnoreCase("LEFT") || move.equalsIgnoreCase("L")) {
            Position currentPosition = p.getPosition();
            if (currentPosition.getCol()-1 < 0) { // checks if out of bounds
                return false;
            } else if (positions[currentPosition.getRow()][currentPosition.getCol()-1].getCharValue() != '-' && // checks if it is a position you can move to
                    !Character.isLowerCase(positions[currentPosition.getRow()][currentPosition.getCol()-1].getCharValue())) { // check if it is a door or not
                return false;
            } else if (Character.isDigit(positions[currentPosition.getRow()][currentPosition.getCol()-1].getCharValue())) { // check if another player is in the new spot
                return false;
            } else {
                return true;
            }
        } else if (move.equalsIgnoreCase("RIGHT") || move.equalsIgnoreCase("R")) {
            Position currentPosition = p.getPosition();
            if (currentPosition.getCol()+1 >= COLUMNS) { // checks if out of bounds
                return false;
            } else if (positions[currentPosition.getRow()][currentPosition.getCol()+1].getCharValue() != '-' && // checks if it is a position you can move to
                    !Character.isLowerCase(positions[currentPosition.getRow()][currentPosition.getCol()+1].getCharValue())) { // check if it is a door or not
                return false;
            } else if (Character.isDigit(positions[currentPosition.getRow()][currentPosition.getCol()+1].getCharValue())) {// check if another player is in the new spot
                return false;
            }else {
                return true;
            }
        } else if (move.equalsIgnoreCase("UP")|| move.equalsIgnoreCase("U")) {
            Position currentPosition = p.getPosition();
            if (currentPosition.getRow()-1 < 0) { // checks if out of bounds
                return false;
            } else if (positions[currentPosition.getRow()-1][currentPosition.getCol()].getCharValue() != '-' && // checks if it is a position you can move to
                    !Character.isLowerCase(positions[currentPosition.getRow()-1][currentPosition.getCol()].getCharValue())) { // check if it is a door or not
                return false;
            } else if (Character.isDigit(positions[currentPosition.getRow()-1][currentPosition.getCol()].getCharValue())) { // check if another player is in the new spot
                return false;
            }else {
                return true;
            }
        } else if (move.equalsIgnoreCase("DOWN")|| move.equalsIgnoreCase("D")) {
            Position currentPosition = p.getPosition();
            if (currentPosition.getRow()+1 >= ROWS) { // checks if out of bounds
                return false;
            } else if (positions[currentPosition.getRow()+1][currentPosition.getCol()].getCharValue() != '-' &&  // checks if it is a position you can move to
                    !Character.isLowerCase(positions[currentPosition.getRow()+1][currentPosition.getCol()].getCharValue())) { // check if it is a door or not
                return false;
            } else if (Character.isDigit(positions[currentPosition.getRow()+1][currentPosition.getCol()-1].getCharValue())) { // check if another player is in the new spot
                return false;
            } else {
                return true;
            }
        } else if (move.equalsIgnoreCase("EXIT")|| move.equalsIgnoreCase("E")) {
            Position newPos = p.getRoom().getDoors().get(0);
            if (false) {
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * moves a Item from one position to another or from one position to inside of a room
     *
     * @param oldPosition - current position of the Item
     * @param newPosition - position Item wants to move to
     * @param item - the Item wanting to move
     */
    public void move(Position oldPosition, Position newPosition, Item item) { //ONLY IF EMPTY SLOT IN HALLWAY
        if (!item.isInRoom()) {
            if (positions[newPosition.getRow()][newPosition.getCol()].getCharValue() == '-') {
                Position temp = positions[oldPosition.getRow()][oldPosition.getCol()];
                positions[newPosition.getRow()][newPosition.getCol()].setItem(temp.getItem());
                positions[newPosition.getRow()][newPosition.getCol()].setCharValue(temp.getCharValue());
                positions[oldPosition.getRow()][oldPosition.getCol()].setItem(null);
                positions[oldPosition.getRow()][oldPosition.getCol()].setCharValue('-');
                item.setPosition(newPosition);
            } else if (Character.isLowerCase(positions[newPosition.getRow()][newPosition.getCol()].getCharValue())) { //if it's a door
                String letter = Character.toString(positions[newPosition.getRow()][newPosition.getCol()].getCharValue()); //convert letter to string

                for (Room r : rooms) {
                    String rLetter = r.getLetter().toString();
                    if (rLetter.equals(letter)) {

                        // new position is actually the next empty slot in the room not the door
                        Position newPosInRoom = null;
                        int roomPosNumber = 0;
                        for (int i = 0; i < r.getRoomPositions().size(); i++) {
                            Position p = r.getRoomPositions().get(i);
                            if (p.getItem() == null) {
                                newPosInRoom = p;
                                roomPosNumber = i;
                                break;
                            }
                        }
                        Position temp = positions[oldPosition.getRow()][oldPosition.getCol()];
                        positions[newPosInRoom.getRow()][newPosInRoom.getCol()].setItem(temp.getItem());
                        positions[newPosInRoom.getRow()][newPosInRoom.getCol()].setCharValue(temp.getCharValue());
                        positions[oldPosition.getRow()][oldPosition.getCol()].setItem(null);
                        positions[oldPosition.getRow()][oldPosition.getCol()].setCharValue('-');
                        r.getRoomPositions().get(roomPosNumber).setItem(item);
                        r.getRoomPositions().get(roomPosNumber).setCharValue(item.getChar());
                        item.setPosition(newPosInRoom);
                        item.setInRoom(true);
                        item.setRoom(r);
                        System.out.println(r.toString());
                    }
                }
            }
        }

        else {
            Position p = item.getRoom().getDoors().get(0);
            Position newPos = null;
            if (positions[p.getRow()+1][p.getCol()].getCharValue() == '-') {
                newPos = positions[p.getRow()+1][p.getCol()];
            } else if (positions[p.getRow()-1][p.getCol()].getCharValue() == '-') {
                newPos = positions[p.getRow()-1][p.getCol()];
            } else if (positions[p.getRow()][p.getCol()+1].getCharValue() == '-'){
                newPos = positions[p.getRow()][p.getCol()+1];
            } else if (positions[p.getRow()][p.getCol()-1].getCharValue() == '-') {
                newPos = positions[p.getRow()][p.getCol()-1];
            } else {
                //Cant exit room as its blocked
                return;
            }

            Position temp = positions[oldPosition.getRow()][oldPosition.getCol()];
            positions[newPos.getRow()][newPos.getCol()].setItem(temp.getItem());
            positions[newPos.getRow()][newPos.getCol()].setCharValue(temp.getCharValue());
            positions[oldPosition.getRow()][oldPosition.getCol()].setItem(null);
            positions[oldPosition.getRow()][oldPosition.getCol()].setCharValue(item.getRoom().getLetter().toString().charAt(0));
            item.setPosition(newPos);
            item.setInRoom(false);
            item.setInRoom(false);
            item.setRoom(null);
        }
    }


    /*--------------------------------*/
    /*       GETTERS AND SETTERS      */
    /*--------------------------------*/

    /**
     * gets the values of the board
     *
     * @return 2D array representing the board
     */
    public Position[][] getPositions() {
        return positions;
    }


    /**
     * gets the number of players
     *
     * @return int representing the number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * gets the list of players
     * @return ArrayList of players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * adds a player to the list of players
     *
     * @param p - player to be added to the list
     */
    public void addPlayer(Player p) {
        players.add(p);
    }

    /**
     * gets the list of weapons
     *
     * @return - list of weapons
     */
    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * gets the list of rooms
     *
     * @return - of of rooms
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }
}
