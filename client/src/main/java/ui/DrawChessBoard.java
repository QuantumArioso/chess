package ui;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private static final int BOARD_WIDTH = 8;

    public static void drawBoard(ChessBoard chessBoard) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        String letters = "   a  b  c  d  e  f  g  h   ";
        if (needToFlip) {
            letters = "   h  g  f  e  d  c  b  a   ";
        }

        drawText(out, letters);
        for (int col = 1; col <= BOARD_WIDTH; col++) {
            out.print(9 - col + " ");
            drawRowOfSquares(out, col % 2 == 0, col);
            out.println(" " + (9 - col));
        }
        drawText(out, letters);
    }

    private static void drawText(PrintStream out, String text) {
        out.println(text);
    }

    private static void drawRowOfSquares(PrintStream out, boolean lighterFirst, int col) {
        if (lighterFirst) {
            for (int row = 1; row <= BOARD_WIDTH; row++) {
                if (row % 2 == 0) {
                    setBlue(out);
                } else {
                    setMagenta(out);
                }
                printPlayer(out, mapPiece(out, col, row));
            }
        } else {
            for (int row = 1; row <= BOARD_WIDTH; row++) {
                if (row % 2 == 0) {
                    setMagenta(out);
                } else {
                    setBlue(out);
                }
                printPlayer(out, mapPiece(out, col, row));
            }
        }
        resetColor(out);
    }

    private static String mapPiece(PrintStream out, int row, int col) {
        if (row != 1 && row != 2 && row != 7 && row != 8) {
            return EMPTY;
        }

        if (row == 1 || row == 2) {
            out.print(SET_TEXT_COLOR_BLACK);

            if (row == 2) {
                return BLACK_PAWN;
            } else {
                return getString(col, BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING);
            }
        } else {
            out.print(SET_TEXT_COLOR_WHITE);

            if (row == 7) {
                return WHITE_PAWN;
            } else return getString(col, WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING);
        }
    }

    private static String getString(int col, String rook, String knight, String bishop, String queen, String king) {
        if (col == 1 || col == 8) {
            return rook;
        } else if (col == 2 || col == 7) {
            return knight;
        } else if (col == 3 || col == 6) {
            return bishop;
        } else if (col == 4) {
            return queen;
        } else {
            return king;
        }
    }

    private static void setMagenta(PrintStream out) {
        out.print(SET_BG_COLOR_MAGENTA);
        out.print(SET_TEXT_COLOR_MAGENTA);
    }

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void resetColor(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(player);
    }
}
