import java.util.ArrayList;
import java.util.List;

/**
 * rooms in the game
 *
 * @author Joshua Richards 300402562 | Melina Arianyi
 */
public class Room {

    enum RoomLetter {b, c, i, l, s, h, g, d, k} // enum of the room letters
    private ArrayList<Position> roomPositions = new ArrayList<>(); // contains all the weapons and players n stuff
    private ArrayList<Position> doors = new ArrayList<>(); // list of all the doors for the room
    private RoomLetter letter; // corresponding letter to the room

    /**
     * creates a new room object
     *
     * @param letter - roomletter that corresponds to the room
     */
    public Room(RoomLetter letter) {
        this.letter = letter;
    }

    /**
     * adds a position to the list of positions
     *
     * @param p - position to add to the list of positions
     */
    public void addPosition(Position p) {
        roomPositions.add(p);
    }

    /**
     * get the list of positions inside the room
     *
     * @return ArrayList of the positions in the room
     */
    public ArrayList<Position> getRoomPositions() {
        return roomPositions;
    }

    /**
     * adds a door to the list of doors (door is a position)
     *
     * @param p - position to add to the list of doors
     */
    public void addDoor(Position p) { doors.add(p);}

    /**
     * gets the list of door for the room
     *
     * @return list of doors
     */
    public ArrayList<Position> getDoors() { return doors; }

    /**
     * corresponding letter to the room
     *
     * @return roomletter to the room
     */
    public RoomLetter getLetter() {
        return letter;
    }

    /**
     * string representation of the room
     *
     * @return string presentation of the room
     */
    public String toString() {
        if (letter.toString().equals("b")) {
            return "BallRoom";
        } else if (letter.toString().equals("c")) {
            return "Conservatory";
        } else if (letter.toString().equals("i")) {
            return "BilliardRoom";
        } else if (letter.toString().equals("l")) {
            return "Library";
        } else if (letter.toString().equals("s")) {
            return "Study";
        } else if (letter.toString().equals("h")) {
            return "Hall";
        } else if (letter.toString().equals("g")) {
            return "Lounge";
        } else if (letter.toString().equals("d")) {
            return "DiningRoom";
        } else if (letter.toString().equals("k")) {
            return "Kitchen";
        }
        return "Room toString messed up";
    }
}
