package chess;

import java.util.Collection;
import java.util.ArrayList;

public class BishopMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int y = position.getRow();
        int x = position.getColumn();

        // can move forwards, backwards, left, or right, in every space up to blocked or edge of board
        // if blocked by enemy, can move to that space and capture (but not further than that space)
        possibleMoves.addAll(findMoves(board, position, y, x, -1, -1));
        possibleMoves.addAll(findMoves(board, position, y, x, -1, 1));
        possibleMoves.addAll(findMoves(board, position, y, x, 1, -1));
        possibleMoves.addAll(findMoves(board, position, y, x, 1, 1));

        return possibleMoves;
    }
}
