
/**
 *
 * @author Joshua Richards 300402562 | Melina Arianyi
 */
public class Position {

    private int row; // positions row on the board
    private int col; // position column on the board
    private Item item; // item stored at this position (null if there isn't an item there)
    private char charValue; // the ascii value that will be printed out

    /**
     * creates a new position object
     *
     * @param row - positions row on the board
     * @param col - position column on the board
     * @param charValue - the ascii value that will be printed out
     * @param item - item stored at this position (null if there isn't an item there)
     */
    public Position(int row, int col, char charValue, Item item) {
        this.row = row;
        this.col = col;
        this.charValue = charValue;
        this.item = item;
    }

    /**
     * gets the row value
     *
     * @return int representing the row
     */
    public int getRow() {
        return row;
    }

    /**
     * sets the row value
     *
     * @param row - int representing the row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * gets the column value
     *
     * @return - int representing the column
     */
    public int getCol() {
        return col;
    }

    /**
     * sets the column value
     *
     * @param col - int representing the column
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * gets the character value
     *
     * @return char representing the character value
     */
    public char getCharValue() {
        return charValue;
    }

    /**
     * sets the character value
     *
     * @param charValue - char representing a character value
     */
    public void setCharValue(char charValue) {
        this.charValue = charValue;
    }

    /**
     * gets the item object
     *
     * @return - item representing the positions item
     */
    public Item getItem() {
        return item;
    }

    /**
     * sets the item object
     *
     * @param item - item representing the positions item
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * string outputting the row, column and character value
     *
     * @return string representing the values of the position
     */
    public String toString() {
        return "row: " + row + ", col: " + col + ", char: " + charValue;
    }
}
