package handler;

import chess.ChessGame;
import chess.ChessPiece;

public record GameJoinRequest(String authToken, ChessGame.TeamColor playerColor, int gameID) {
}
