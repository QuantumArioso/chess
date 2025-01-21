package chess;

import java.util.Collection;
import java.util.ArrayList;

public class RookMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ArrayList<ChessPosition> possiblePositions = new ArrayList<>();
        int y = position.getRow();
        int x = position.getColumn();

        // can move forwards, backwards, left, or right, in every space up to blocked or edge of board
        // if blocked by enemy, can move to that space and capture (but not further than that space)
        possiblePositions.addAll(findMoves(board, position, y, x, -1, 0));
        possiblePositions.addAll(findMoves(board, position, y, x, 1, 0));
        possiblePositions.addAll(findMoves(board, position, y, x, 0, -1));
        possiblePositions.addAll(findMoves(board, position, y, x, 0, 1));

        for (ChessPosition possiblePosition : possiblePositions) {
            ChessMove move = new ChessMove(position, possiblePosition, null);
            possibleMoves.add(move);
        }

        return possibleMoves;
    }

    /**
     *
     * @param board chess board located on
     * @param y current position row
     * @param x current position column
     * @param xDirection -1 for left, +1 for right, 0 for neither
     * @param yDirection -1 for south, +1 for north, 0 for neither
     * @return ArrayList of possible positions
     */
    public Collection<ChessPosition> findMoves(ChessBoard board, ChessPosition oldPosition, int y, int x, int xDirection, int yDirection) {
        y += yDirection;
        x += xDirection;
        ChessPosition newPosition = new ChessPosition(y, x);
        ArrayList<ChessPosition> newPositions = new ArrayList<>();

        while (board.inBounds(newPosition) && board.isEmpty(newPosition)) {
            newPositions.add(newPosition);
            y += yDirection;
            x += xDirection;
            newPosition = new ChessPosition(y, x);
        }
        // check if can capture
        if (board.inBounds(newPosition) && board.isNotOccupiedBySelf(oldPosition, newPosition)) {
            newPositions.add(newPosition);
        }

        return newPositions;
    }
}