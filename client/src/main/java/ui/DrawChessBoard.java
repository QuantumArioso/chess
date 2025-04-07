package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Math.abs;
import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private static final int BOARD_WIDTH = 8;

    public static void drawBoard(ChessGame game, boolean needToFlip, ChessPosition pos) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        int flipInt = 0;

        String letters = "   h  g  f  e  d  c  b  a   ";
        boolean lighterFirst = false;
        if (needToFlip) {
            letters = "   a  b  c  d  e  f  g  h   ";
            lighterFirst = true;
            flipInt = 9;
        }

        drawText(out, letters);
        Collection<ChessPosition> moveLocs = getHighlightSpots(game, pos);
        for (int row = 1; row <= BOARD_WIDTH; row++) {
            out.print(9 - abs(9 - flipInt - row) + " ");
            drawRowOfSquares(out, game.getBoard(), lighterFirst, abs(flipInt - row), flipInt, moveLocs);
            out.println(" " + (9 - abs(9 - flipInt - row)));
            lighterFirst = !lighterFirst;
        }
        drawText(out, letters);
    }

    private static Collection<ChessPosition> getHighlightSpots(ChessGame game, ChessPosition pos) {
        if (pos == null) {
            return new ArrayList<>();
        }
        Collection<ChessMove> validMoves = game.validMoves(pos);
        Collection<ChessPosition> validPoses = new ArrayList<>();
        for (ChessMove move : validMoves) {
            validPoses.add(move.getEndPosition());
        }
        return validPoses;
    }

    private static void drawText(PrintStream out, String text) {
        out.println(text);
    }

    private static void drawRowOfSquares(PrintStream out, ChessBoard board, boolean lighterFirst, int row,
                                         int flipInt, Collection<ChessPosition> moveLocs) {
        if (flipInt == 0) {
            if (lighterFirst) {
                for (int col = BOARD_WIDTH; col > 0; col--) {
                    if (!moveLocs.isEmpty() && moveLocs.contains(new ChessPosition(row, col))) {
                        if (col % 2 != 0) {
                            setGreen(out);
                        } else {
                            setDarkGreen(out);
                        }
                    } else {
                        if (col % 2 != 0) {
                            setBlue(out);
                        } else {
                            setMagenta(out);
                        }
                    }
                    printPlayer(out, mapPiece(out, board, new ChessPosition(row, col)));
                }
            } else {
                for (int col = BOARD_WIDTH; col > 0; col--) {
                    if (!moveLocs.isEmpty() && moveLocs.contains(new ChessPosition(row, col))) {
                        if (col % 2 != 0) {
                            setDarkGreen(out);
                        } else {
                            setGreen(out);
                        }
                    } else {
                        if (col % 2 != 0) {
                            setMagenta(out);
                        } else {
                            setBlue(out);
                        }
                    }
                    printPlayer(out, mapPiece(out, board, new ChessPosition(row, col)));
                }
            }
        } else {
            if (lighterFirst) {
                for (int col = 1; col <= BOARD_WIDTH; col++) {
                    if (!moveLocs.isEmpty() && moveLocs.contains(new ChessPosition(row, col))) {
                        if (col % 2 != 0) {
                            setGreen(out);
                        } else {
                            setDarkGreen(out);
                        }
                    } else {
                        if (col % 2 != 0) {
                            setBlue(out);
                        } else {
                            setMagenta(out);
                        }
                    }
                    printPlayer(out, mapPiece(out, board, new ChessPosition(row, col)));
                }
            } else {
                for (int col = 1; col <= BOARD_WIDTH; col++) {
                    if (!moveLocs.isEmpty() && moveLocs.contains(new ChessPosition(row, col))) {
                        if (col % 2 != 0) {
                            setDarkGreen(out);
                        } else {
                            setGreen(out);
                        }
                    } else {
                        if (col % 2 != 0) {
                            setMagenta(out);
                        } else {
                            setBlue(out);
                        }
                    }
                    printPlayer(out, mapPiece(out, board, new ChessPosition(row, col)));
                }
            }
        }
        resetColor(out);
    }

    private static String mapPiece(PrintStream out, ChessBoard board, ChessPosition pos) {
        if (!board.isEmpty(pos)) {
            ChessGame.TeamColor color = board.getPiece(pos).getTeamColor();
            if (color.equals(ChessGame.TeamColor.WHITE)) {
                out.print(SET_TEXT_COLOR_WHITE);
            } else {
                out.print(SET_TEXT_COLOR_BLACK);
            }
            ChessPiece.PieceType piece = board.getPiece(pos).getPieceType();
            return getPieceString(color, piece);
        }
        return EMPTY;
    }

    private static String getPieceString(ChessGame.TeamColor color, ChessPiece.PieceType piece) {
        if (color.equals(ChessGame.TeamColor.WHITE)) {
            return switch (piece) {
                case PAWN -> WHITE_PAWN;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case ROOK -> WHITE_ROOK;
                case QUEEN -> WHITE_QUEEN;
                default -> WHITE_KING;
            };
        } else {
            return switch (piece) {
                case PAWN -> BLACK_PAWN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case QUEEN -> BLACK_QUEEN;
                default -> WHITE_KING;
            };
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

    private static void setGreen(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    private static void setDarkGreen(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
    }

    private static void resetColor(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(player);
    }
}
