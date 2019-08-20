
/**
 * class representing a weapon in the game
 *
 * @author Joshua Richards 300402562 | Melina Arianyi
 */
public class Weapon implements Item{

    enum WeaponName {Candlestick, Dagger, LeadPipe, Revolver, Rope, Spanner} // enum of all the possible weapons
    private WeaponName name; // weapons name
    private Room room; // weapons room
    private Position pos; // current position of the weapon
    private char charValue; // character value representing the weapon
    private boolean inRoom; // taken from item

    /**
     * creates a new weapon object and allocates its corresponding character
     *
     * @param name - weaponname of weapon
     */
    public Weapon (WeaponName name) {
        this.name = name;
        if (name.toString().equals("Candlestick")) {
            charValue = '|';
        } else if (name.toString().equals("Dagger")) {
            charValue = '+';
        } else if (name.toString().equals("LeadPipe")) {
            charValue = '!';
        } else if (name.toString().equals("Revolver")) {
            charValue = '*';
        } else if (name.toString().equals("Rope")) {
            charValue = '%';
        } else if (name.toString().equals("Spanner")) {
            charValue = '^';
        }
    }

    /**
     * gets the name of the weapon
     *
     * @return - weaponname of the weapon
     */
    public WeaponName getName() {
        return name;
    }

    /**
     * gets the position on the board of the weapon
     *
     * @return position of the weapon
     */
    @Override public Position getPosition() {
        return this.pos;
    }

    /**
     * sets the position on the board of the weapon
     *
     * @param pos - position to be set as the position of the weapon
     */
    @Override public void setPosition(Position pos) {
        this.pos = pos;
    }

    /**
     * gets the weapons room
     *
     * @return room the weapon is in
     */
    @Override public Room getRoom() {
        return room;
    }

    /**
     * sets the room the weapon will be in
     *
     * @param room - room that the item will be in
     */
    @Override public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * gets the corresponding character to the weapon
     *
     * @return char of the weapon
     */
    @Override public char getChar() {
        return charValue;
    }

    /**
     * sets whether or not the weapon is in a room
     *
     * @param inRoom - boolean if the weapon is in the room or not
     */
    @Override public void setInRoom(boolean inRoom) {
        this.inRoom = inRoom;
    }

    /**
     * from item interface
     *
     * @return boolean
     */
    public boolean isInRoom() {
        return inRoom;
    }

    /**
     * string representation of the weapon
     *
     * @return string representation of the weapon
     */
    @Override public String toString() {
        return name.toString();
    }
}
