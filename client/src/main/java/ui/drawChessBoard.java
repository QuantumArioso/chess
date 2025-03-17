package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
import chess.ChessGame;

public class drawChessBoard {
    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        drawSquare(out, "Magenta");
    }

    private static void drawRowOfSquares(PrintStream out) {

    }

    private static void drawSquare(PrintStream out, String color) {
        setMagenta(out);
        out.print(EMPTY);
    }

    private static void setMagenta(PrintStream out) {
        out.print(SET_BG_COLOR_MAGENTA);
        out.print(SET_TEXT_COLOR_MAGENTA);
    }

    private static void setYellow(PrintStream out) {
        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_COLOR_YELLOW);
    }

    private static void printPlayer(PrintStream out, ChessGame.TeamColor player) {

    }
}
