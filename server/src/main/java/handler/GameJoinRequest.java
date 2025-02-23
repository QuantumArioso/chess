package handler;

import chess.ChessGame;
import chess.ChessPiece;

public record GameJoinRequest(ChessGame.TeamColor playerColor, String gameID) {
}
