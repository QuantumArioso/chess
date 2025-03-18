package ui;

import chess.ChessBoard;

public class Client {
    // depends on DrawChessBoard
    // depends on ServerFacade
    public static void main(String args[]) {
        ChessBoard board =  new ChessBoard();

        DrawChessBoard.drawBoard(board);
    }
}
