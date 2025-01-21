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
public class ChessPiece {

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
        if (pieceColor == ChessGame.TeamColor.BLACK) {
            if (type == ChessPiece.PieceType.KING) {
                return "k";
            }
            else if (type == ChessPiece.PieceType.QUEEN) {
                return "q";
            }
            else if (type == ChessPiece.PieceType.BISHOP) {
                return "b";
            }
            else if (type == ChessPiece.PieceType.KNIGHT) {
                return "n";
            }
            else if (type == ChessPiece.PieceType.ROOK) {
                return "r";
            }
            else {
                return "p";
            }
        }
        else {
            if (type == ChessPiece.PieceType.KING) {
                return "K";
            }
            else if (type == ChessPiece.PieceType.QUEEN) {
                return "Q";
            }
            else if (type == ChessPiece.PieceType.BISHOP) {
                return "B";
            }
            else if (type == ChessPiece.PieceType.KNIGHT) {
                return "N";
            }
            else if (type == ChessPiece.PieceType.ROOK) {
                return "R";
            }
            else {
                return "P";
            }
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
        Collection<ChessMove> moves;
        if (type == ChessPiece.PieceType.KING) {
            KingMovesCalculator calculator = new KingMovesCalculator();
            moves = calculator.pieceMoves(board, myPosition);
        }
        else if (type == ChessPiece.PieceType.PAWN) {
            PawnMovesCalculator calculator = new PawnMovesCalculator();
            moves = calculator.pieceMoves(board, myPosition);
        }
        else if (type == ChessPiece.PieceType.ROOK) {
            RookMovesCalculator calculator = new RookMovesCalculator();
            moves = calculator.pieceMoves(board, myPosition);
        }
        else {
            moves = new ArrayList<>();
        }

        return moves;
    }
}
