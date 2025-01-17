package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    // see this diagram: https://github.com/softwareconstruction240/softwareconstruction/blob/main/chess/0-chess-moves/ChessPhase0Design.png
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);
}
