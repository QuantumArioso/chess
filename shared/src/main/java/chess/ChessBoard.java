package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {
    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 7; i >= 0; i--) {
            output += "|";
            for (int j = 0; j < 8; j++) {
                if (squares[i][j] != null) {
                    output += squares[i][j].toString() + "|";
                }
                else {
                    output += " |";
                }
            }
            output += "\n";
        }
        return output;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        // the positions passed in are 1-8; my indexing is 0-7
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    private void setNewSquares(ChessPiece[][] newSquares) {
        squares = newSquares;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
        // puts pieces where they belong when game starts
        squares[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        squares[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        squares[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        squares[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        squares[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        squares[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < 8; i++) {
            squares[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }

    /**
     * Checks if given position is valid
     * @param position position to check
     * @return true if in bounds, false otherwise
     */
    public boolean inBounds(ChessPosition position) {
        int x = position.getRow();
        int y = position.getColumn();
        return x > 0 && x <= squares.length && y > 0 && y <= squares[0].length;
    }

    /**
     * Checks if given position is occupied by a piece on your own team
     * @param oldPosition current position
     * @param newPosition position to check
     * @return true if empty or occupied by enemy, false otherwise
     */
    public boolean isNotOccupiedBySelf(ChessPosition oldPosition, ChessPosition newPosition) {
        if (getPiece(newPosition) == null) {
            return true;
        }

        ChessPiece piece = getPiece(newPosition);
        ChessGame.TeamColor oldColor = getPiece(oldPosition).getTeamColor();
        ChessGame.TeamColor newColor = piece.getTeamColor();
        return oldColor != newColor;
    }

    /**
     * Checks if given position is not occupied
     * @param position position to check
     * @return true if empty, false otherwise
     */
    public boolean isEmpty(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1] == null;
    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();

            ChessPiece[][] clonedSquares = new ChessPiece[8][8];
            clone.setNewSquares(clonedSquares);
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition pos = new ChessPosition(i, j);
                    ChessPiece piece = getPiece(pos);
                    if (piece != null) {
                        clone.addPiece(pos, piece.clone());
                    }
                    else {
                        clone.addPiece(pos, null);
                    }
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
