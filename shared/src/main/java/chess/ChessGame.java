package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor currentTeam;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard(); // TODO: should I be doing this?
        currentTeam = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);
        return helperIsInCheck(teamColor, kingPosition);
    }

    /**
     * Used to check if any position would be in check if the king was there
     * @param teamColor the team the king is on
     * @param kingPosition the position the king is/would be at
     * @return true if that position is in check
     */
    private boolean helperIsInCheck(TeamColor teamColor, ChessPosition kingPosition) {
        TeamColor enemyColor = getEnemyTeam(teamColor);
        Collection<ChessPosition> enemyPositions = findAllTeamPiecePositions(enemyColor);

        for (ChessPosition pos : enemyPositions) {
            ChessPiece piece = board.getPiece(pos);
            if (piece != null) {
                Collection<ChessMove> moves = piece.pieceMoves(board, pos);
                for (ChessMove move : moves) {
                    if (move.getEndPosition().equals(kingPosition)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Searches the board for the current team's king
     * @return the position of current team's king
     */
    private ChessPosition findKingPosition(TeamColor team) {
        Collection<ChessPosition> positions = findAllTeamPiecePositions(team);
        for (ChessPosition pos : positions) {
            if (board.getPiece(pos).getPieceType().equals(ChessPiece.PieceType.KING)) {
                return pos;
            }
        }
        return null;
    }

    /**
     * Finds all of a team's pieces' positions
     * @param team the color for who's pieces to find
     * @return the positions of where every piece for a team is
     */
    private Collection<ChessPosition> findAllTeamPiecePositions(TeamColor team) {
        Collection<ChessPosition> positions = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor().equals(team)) {
                    positions.add(pos);
                }
            }
        }
        return positions;
    }

    /**
     * Calculates all possible moves that all pieces from a specified team can make, ignoring check rules
     * @param team the color for who's moves to calculate
     * @return all possible moves a team can make
     */
    private Collection<ChessMove> allPossibleTeamMoves(TeamColor team) {
        Collection<ChessPosition> positions = findAllTeamPiecePositions(team);
        Collection<ChessMove> moves = new ArrayList<>();

        for (ChessPosition pos : positions) {
            ChessPiece piece = board.getPiece(pos);
            moves.addAll(piece.pieceMoves(board, pos));
        }

        return moves;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        TeamColor enemyColor = getEnemyTeam(teamColor);
        ChessBoard myBoard = board;
        Collection<ChessMove> allMyMoves = allPossibleTeamMoves(teamColor);
        Collection<ChessPosition> allMyPossiblePositions = new ArrayList<>();
        for (ChessMove move : allMyMoves) {
            allMyPossiblePositions.add(move.getEndPosition());
        }

        Collection<ChessMove> allEnemyMoves = allPossibleTeamMoves(enemyColor);
//        Collection<ChessPosition> allEnemyPossiblePositions = new ArrayList<>();
//        for (ChessMove move : allEnemyMoves) {
//            allEnemyPossiblePositions.add(move.getEndPosition());
//        }

        ChessPosition kingPosition = findKingPosition(teamColor);
        if (kingPosition == null) {
            throw new RuntimeException("Somehow you lost your king. How did you do this?");
        }
        ChessPiece king = board.getPiece(kingPosition);
        Collection<ChessMove> kingPossibleMoves = king.pieceMoves(board, kingPosition);
        Collection<ChessPosition> kingPossiblePositions = new ArrayList<>();
        for (ChessMove move : kingPossibleMoves) {
            kingPossiblePositions.add(move.getEndPosition());
        }

        Collection<ChessPosition> spotsInCheck = new ArrayList<>();
        if (isInCheck(teamColor)) {
            spotsInCheck.add(kingPosition);
        }
        for (ChessPosition pos : kingPossiblePositions) {
            if (helperIsInCheck(teamColor, pos)) {
                spotsInCheck.add(pos);
            }
        }

        boolean stillInCheck = false;
        for (ChessPosition checkPos : spotsInCheck) {
            for (ChessMove enemyMove : allEnemyMoves) {
                if (enemyMove.getEndPosition().equals(checkPos) &&
                        !allMyPossiblePositions.contains(enemyMove.getStartPosition())) {
                    stillInCheck = true;
                    break;
                }
            }
        }
        return stillInCheck;

//        // if there's somewhere the king can move that's not where the enemy can, we're not in checkmate
//        if (!allEnemyPossiblePositions.containsAll(kingPossiblePositions)) {
//            return false;
//        }
//
//        // if the enemy can't move to where the king is or where the king can move, we're not in checkmate
//
//
//
//        if (allEnemyPossiblePositions.contains(kingPosition) &&
//                allEnemyPossiblePositions.containsAll(kingPossiblePositions)) {
//            for (ChessMove enemyMove : allEnemyMoves) {
//                for (ChessPosition myPosition : allMyPossiblePositions) {
//                    if (enemyMove.getEndPosition().equals(kingPosition) && enemyMove.getStartPosition().equals(myPosition)) {
//                        // TODO: but what if it's still in checkmate from everything else?
//                        return false;
//                    }
//                }
//            }
//
//
//            if () {
//                // TODO: and I can't capture them so the king is no longer in danger
//                return false;
//            }
//
//        }


    }

    /**
     * Returns the enemy team
     * @param myTeam team color
     * @return the color of the opposing team
     */
    private TeamColor getEnemyTeam(TeamColor myTeam) {
        if (myTeam.equals(TeamColor.BLACK)) {
            return TeamColor.WHITE;
        }
        return TeamColor.BLACK;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
