package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    private ChessPiece bKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
    private ChessPiece bQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
    private ChessPiece bBishopA = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
    private ChessPiece bBishopB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
    private ChessPiece bKnightA = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
    private ChessPiece bKnightB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
    private ChessPiece bRookA = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    private ChessPiece bRookB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    private ChessPiece bPawnA = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
    private ChessPiece bPawnB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
    private ChessPiece bPawnC = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
    private ChessPiece bPawnD = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
    private ChessPiece bPawnE = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
    private ChessPiece bPawnF = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
    private ChessPiece bPawnG = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
    private ChessPiece bPawnH = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
    private ChessPiece wKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
    private ChessPiece wQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
    private ChessPiece wBishopA = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
    private ChessPiece wBishopB = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
    private ChessPiece wKnightA = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
    private ChessPiece wKnightB = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
    private ChessPiece wRookA = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    private ChessPiece wRookB = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    private ChessPiece wPawnA = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
    private ChessPiece wPawnB = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
    private ChessPiece wPawnC = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
    private ChessPiece wPawnD = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
    private ChessPiece wPawnE = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
    private ChessPiece wPawnF = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
    private ChessPiece wPawnG = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
    private ChessPiece wPawnH = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

    public ChessBoard() {

    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < 8; i++) {
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
        // puts pieces where they belong when game starts
        squares[0][0] = wRookA;
        squares[0][1] = wKnightA;
        squares[0][2] = wBishopA;
        squares[0][3] = wQueen;
        squares[0][4] = wKing;
        squares[0][5] = wBishopB;
        squares[0][6] = wKnightB;
        squares[0][7] = wRookB;
        squares[1][0] = wPawnA;
        squares[1][1] = wPawnB;
        squares[1][2] = wPawnC;
        squares[1][3] = wPawnD;
        squares[1][4] = wPawnE;
        squares[1][5] = wPawnF;
        squares[1][6] = wPawnG;
        squares[1][7] = wPawnH;
        squares[6][0] = bPawnA;
        squares[6][1] = bPawnB;
        squares[6][2] = bPawnC;
        squares[6][3] = bPawnD;
        squares[6][4] = bPawnE;
        squares[6][5] = bPawnF;
        squares[6][6] = bPawnG;
        squares[6][7] = bPawnH;
        squares[7][0] = bRookA;
        squares[7][1] = bKnightA;
        squares[7][2] = bBishopA;
        squares[7][3] = bQueen;
        squares[7][4] = bKing;
        squares[7][5] = bBishopB;
        squares[7][6] = bKnightB;
        squares[7][7] = bRookB;
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
        return Objects.deepEquals(squares, that.squares) && Objects.equals(bKing, that.bKing) && Objects.equals(bQueen, that.bQueen) && Objects.equals(bBishopA, that.bBishopA) && Objects.equals(bBishopB, that.bBishopB) && Objects.equals(bKnightA, that.bKnightA) && Objects.equals(bKnightB, that.bKnightB) && Objects.equals(bRookA, that.bRookA) && Objects.equals(bRookB, that.bRookB) && Objects.equals(bPawnA, that.bPawnA) && Objects.equals(bPawnB, that.bPawnB) && Objects.equals(bPawnC, that.bPawnC) && Objects.equals(bPawnD, that.bPawnD) && Objects.equals(bPawnE, that.bPawnE) && Objects.equals(bPawnF, that.bPawnF) && Objects.equals(bPawnG, that.bPawnG) && Objects.equals(bPawnH, that.bPawnH) && Objects.equals(wKing, that.wKing) && Objects.equals(wQueen, that.wQueen) && Objects.equals(wBishopA, that.wBishopA) && Objects.equals(wBishopB, that.wBishopB) && Objects.equals(wKnightA, that.wKnightA) && Objects.equals(wKnightB, that.wKnightB) && Objects.equals(wRookA, that.wRookA) && Objects.equals(wRookB, that.wRookB) && Objects.equals(wPawnA, that.wPawnA) && Objects.equals(wPawnB, that.wPawnB) && Objects.equals(wPawnC, that.wPawnC) && Objects.equals(wPawnD, that.wPawnD) && Objects.equals(wPawnE, that.wPawnE) && Objects.equals(wPawnF, that.wPawnF) && Objects.equals(wPawnG, that.wPawnG) && Objects.equals(wPawnH, that.wPawnH);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(squares), bKing, bQueen, bBishopA, bBishopB, bKnightA, bKnightB, bRookA, bRookB, bPawnA, bPawnB, bPawnC, bPawnD, bPawnE, bPawnF, bPawnG, bPawnH, wKing, wQueen, wBishopA, wBishopB, wKnightA, wKnightB, wRookA, wRookB, wPawnA, wPawnB, wPawnC, wPawnD, wPawnE, wPawnF, wPawnG, wPawnH);
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

}
