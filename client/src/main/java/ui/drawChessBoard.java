package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
import chess.ChessGame;

public class drawChessBoard {
    private static final int BOARD_WIDTH = 8;
    private static final int SQUARE_WIDTH = 2;
    private static final int SQUARE_HEIGHT = 3;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        drawBoard(out);
    }

    private static void drawBoard(PrintStream out) {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            drawRowOfSquares(out, i % 2 == 0);
        }
    }

    private static void drawRowOfSquares(PrintStream out, boolean magentaFirst) {
        if (magentaFirst) {
            for (int i = 0; i < SQUARE_HEIGHT; i++) {
                for (int j = 0; j < BOARD_WIDTH; j++) {
                    if (j % 2 == 0) {
                        setMagenta(out);
                    } else {
                        setBlue(out);
                    }
                    drawSquare(out);
                    if (i == SQUARE_HEIGHT / 2) {
                        printPlayer(out, " P ");
                    } else {
                        printPlayer(out, EMPTY);
                    }
                    drawSquare(out);
                }
                resetColor(out);
                out.println();
            }
        } else {
            for (int i = 0; i < SQUARE_HEIGHT; i++) {
                for (int j = 0; j < BOARD_WIDTH; j++) {
                    if (j % 2 == 0) {
                        setBlue(out);
                    } else {
                        setMagenta(out);
                    }
                    drawSquare(out);
                    printPlayer(out, EMPTY);
                    drawSquare(out);
                }
                resetColor(out);
                out.println();
            }
        }
    }

    private static void drawSquare(PrintStream out) {
        out.print(EMPTY.repeat(SQUARE_WIDTH / 2));
    }

    private static void setMagenta(PrintStream out) {
        out.print(SET_BG_COLOR_MAGENTA);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void resetColor(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(player);
    }
}
