package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ArrayList<ChessPosition> possiblePositions = new ArrayList<>();
        int y = position.getRow();
        int x = position.getColumn();
        ChessPosition newPosition = new ChessPosition(y+1,x-2);

        if (checkSpot(board, position, newPosition)) {
            possiblePositions.add(newPosition);
        }
        newPosition = new ChessPosition(y+2,x-1);
        if (checkSpot(board, position, newPosition)) {
            possiblePositions.add(newPosition);
        }
        newPosition = new ChessPosition(y+2,x+1);
        if (checkSpot(board, position, newPosition)) {
            possiblePositions.add(newPosition);
        }
        newPosition = new ChessPosition(y+1,x+2);
        if (checkSpot(board, position, newPosition)) {
            possiblePositions.add(newPosition);
        }
        newPosition = new ChessPosition(y-1,x+2);
        if (checkSpot(board, position, newPosition)) {
            possiblePositions.add(newPosition);
        }
        newPosition = new ChessPosition(y-2,x+1);
        if (checkSpot(board, position, newPosition)) {
            possiblePositions.add(newPosition);
        }
        newPosition = new ChessPosition(y-2,x-1);
        if (checkSpot(board, position, newPosition)) {
            possiblePositions.add(newPosition);
        }
        newPosition = new ChessPosition(y-1,x-2);
        if (checkSpot(board, position, newPosition)) {
            possiblePositions.add(newPosition);
        }

        for (ChessPosition possiblePosition : possiblePositions) {
            ChessMove move = new ChessMove(position, possiblePosition, null);
            possibleMoves.add(move);
        }

        return possibleMoves;
    }

    public boolean checkSpot(ChessBoard board, ChessPosition oldPosition, ChessPosition newPosition) {
        return board.inBounds(newPosition) && board.isNotOccupiedBySelf(oldPosition, newPosition);
    }
}
