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
}