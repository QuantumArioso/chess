package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        String colLetter = "";
        switch (col) {
            case 1:
                colLetter += "a";
                break;
            case 2:
                colLetter += "b";
                break;
            case 3:
                colLetter += "c";
                break;
            case 4:
                colLetter += "d";
                break;
            case 5:
                colLetter += "e";
                break;
            case 6:
                colLetter += "f";
                break;
            case 7:
                colLetter += "g";
                break;
            case 8:
                colLetter += "h";
                break;
        }

        return String.format("position %s%s", colLetter, row);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }
}
