package chess;

import java.util.Collection;
import java.util.ArrayList;

public class PawnMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ArrayList<ChessPosition> possiblePositions = new ArrayList<>();
        int x = position.getRow();
        int y = position.getColumn();
        int moveDirection = 1;
        ChessPiece pawn = board.getPiece(position);

        if (pawn.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            moveDirection = -1;
        }

        if (moveForward(board, position, moveDirection) != null) {
            possiblePositions.add(moveForward(board, position, moveDirection));
        }

        // moveForward first turn
        if ((moveDirection == 1 && position.getRow() == 2) || (moveDirection == -1 && position.getRow() == 7)) {
            ChessPosition intermediatePosition = new ChessPosition(x+moveDirection, y);
            if (board.isNotOccupiedBySelf(position, intermediatePosition)) {
                possiblePositions.add(moveForward(board, intermediatePosition, moveDirection));
            }
        }

        // capture
        possiblePositions.addAll(capture(board, position, moveDirection));


        for (ChessPosition possiblePosition : possiblePositions) {
            if (possiblePosition != null) {
                if (possiblePosition.getRow() == 1 || possiblePosition.getRow() == 8) {
                    possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.ROOK));
                } else {
                    possibleMoves.add(new ChessMove(position, possiblePosition, null));
                }
            }
        }

        return possibleMoves;
    }

    /**
     * returns the position in front of the pawn that it can move to
     * @param board the chess board the pawn is on
     * @param position the current position of the pawn
     * @param moveDirection the direction the pawn is moving in
     * @return the new position of the pawn, or null if it cannot move forward
     */
    public ChessPosition moveForward(ChessBoard board, ChessPosition position, int moveDirection) {
        ChessPosition newPosition = new ChessPosition(position.getRow() + moveDirection, position.getColumn());
        if (board.inBounds(newPosition) && board.isEmpty(newPosition)) {
            return newPosition;
        }
        return null;
    }

    public Collection<ChessPosition> capture(ChessBoard board, ChessPosition position, int moveDirection) {
        ArrayList<ChessPosition> captures = new ArrayList<>();
        ChessPosition newPositionA = new ChessPosition(position.getRow() + moveDirection, position.getColumn() - 1);
        ChessPosition newPositionB = new ChessPosition(position.getRow() + moveDirection, position.getColumn() + 1);

        if (board.inBounds(newPositionA) && !board.isEmpty(newPositionA) && board.isNotOccupiedBySelf(position, newPositionA)) {
            captures.add(newPositionA);
        }
        if (board.inBounds(newPositionB) && !board.isEmpty(newPositionB) && board.isNotOccupiedBySelf(position, newPositionB)) {
            captures.add(newPositionB);
        }
        return captures;
    }
}
