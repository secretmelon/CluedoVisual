
/**
 *  item object that is either a player or a weapon
 *
 * @author Joshua Richards 300402562 | Melina Arianyi
 */
public interface Item {

    /**
     * gets the position of the item
     *
     * @return - position of the item
     */
    Position getPosition();

    /**
     * sets the position of the item
     *
     * @param pos - position to be set as the position of the item
     */
    void setPosition(Position pos);

    /**
     * gets the room that the item is in
     *
     * @return room the the item is in
     */
    Room getRoom();

    /**
     * set the room that the item will be in
     *
     * @param room - room that the item will be in
     */
    void setRoom(Room room);

    /**
     *if the item is in a room or not
     *
     * @return boolean if the item is in a room or not
     */
    boolean isInRoom();

    /**
     * sets if the item is in the room or not
     *
     * @param inRoom - boolean if the item is in the room or not
     */
    void setInRoom(boolean inRoom);

    /**
     * gets the corresponding char to the item
     *
     * @return char representing the item on the board
     */
    char getChar();

    /**
     * clean string version to represent the item
     *
     * @return string to represent the item
     */
    String toString();
}
