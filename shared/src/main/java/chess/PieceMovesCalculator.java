package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {
    // see this diagram: https://github.com/softwareconstruction240/softwareconstruction/blob/main/chess/0-chess-moves/ChessPhase0Design.png
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    /**
     * Used for rook, bishop, and queen
     * @param board chess board located on
     * @param y current position row
     * @param x current position column
     * @param xDirection -1 for left, +1 for right, 0 for neither
     * @param yDirection -1 for south, +1 for north, 0 for neither
     * @return ArrayList of possible positions
     */
    default Collection<ChessMove> findMoves(ChessBoard board, ChessPosition oldPosition, int y, int x, int xDirection, int yDirection) {
        y += yDirection;
        x += xDirection;
        ChessPosition newPosition = new ChessPosition(y, x);
        ArrayList<ChessPosition> possiblePositions = new ArrayList<>();
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        while (board.inBounds(newPosition) && board.isEmpty(newPosition)) {
            possiblePositions.add(newPosition);
            y += yDirection;
            x += xDirection;
            newPosition = new ChessPosition(y, x);
        }
        // check if can capture
        if (board.inBounds(newPosition) && board.isNotOccupiedBySelf(oldPosition, newPosition)) {
            possiblePositions.add(newPosition);
        }

        for (ChessPosition possiblePosition : possiblePositions) {
            ChessMove move = new ChessMove(oldPosition, possiblePosition, null);
            possibleMoves.add(move);
        }

        return possibleMoves;
    }
}
