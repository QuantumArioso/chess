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
        TeamColor enemyColor = getEnemyTeam(teamColor);

        Collection<ChessPosition> positions = findAllTeamPiecePositions(enemyColor);
        for (ChessPosition pos : positions) {
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
        Collection<ChessMove> allMyMoves = allPossibleTeamMoves(teamColor);
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
