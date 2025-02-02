package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            return switch (type) {
                case KING -> "K";
                case PAWN -> "P";
                case ROOK -> "R";
                case QUEEN -> "Q";
                case BISHOP -> "B";
                default -> "N";
            };
        }
        else {
            return switch (type) {
                case KING -> "k";
                case PAWN -> "p";
                case ROOK -> "r";
                case QUEEN -> "q";
                case BISHOP -> "b";
                default -> "n";
            };
        }
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculator calculator;
        switch (type) {
            case ROOK:
                calculator = new RookMovesCalculator();
                return calculator.pieceMoves(board, myPosition);
            case BISHOP:
                calculator = new BishopMovesCalculator();
                return calculator.pieceMoves(board, myPosition);
            case QUEEN:
                calculator = new QueenMovesCalculator();
                return calculator.pieceMoves(board, myPosition);
            case PAWN:
                calculator = new PawnMovesCalculator();
                return calculator.pieceMoves(board, myPosition);
            case KNIGHT:
                calculator = new KnightMovesCalculator();
                return calculator.pieceMoves(board, myPosition);
            default:
                calculator = new KingMovesCalculator();
                return calculator.pieceMoves(board, myPosition);
        }
    }

    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
