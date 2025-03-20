package ui;

import chess.ChessBoard;
import chess.ChessGame;

public class Client {
    // depends on DrawChessBoard
    // depends on ServerFacade
    public static void main(String args[]) {
        ChessGame game =  new ChessGame();
        ChessBoard board = game.getBoard();
        board.resetBoard();
        DrawChessBoard.drawBoard(board, game.getTeamTurn() == ChessGame.TeamColor.BLACK);
    }
}
