package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Class to calculate the possible positions a king can move to
 */
public class KingMovesCalculator implements PieceMovesCalculator {
    /**
     * Calculates all possible positions the king can move to
     * @param board the chess board that the king exists on
     * @param position the position of the king
     * @return a collection of all possible ChessMoves for the king
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ArrayList<ChessPosition> possiblePositions = new ArrayList<>();
        int x = position.getRow();
        int y = position.getColumn();

        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                ChessPosition newPosition = new ChessPosition(i, j);
                if (!newPosition.equals(position) && board.inBounds(newPosition) && board.isNotOccupiedBySelf(position, newPosition)) {
                    possiblePositions.add(newPosition);
                }
            }
        }

        for (ChessPosition possiblePosition : possiblePositions) {
            ChessMove move = new ChessMove(position, possiblePosition, null);
            possibleMoves.add(move);
        }

        return possibleMoves;
    }
}
