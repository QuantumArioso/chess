package ui;

import chess.ChessBoard;
import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    // depends on DrawChessBoard
    // depends on ServerFacade
    public static void main(String args[]) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(System.in);
        out.print("""
                Welcome to my kingdom! We are in the midst of a war and could use thy assistance.
                What do ye wish to do first?
                """);
        while (true) {
            boolean loggedIn = false;
            while (!loggedIn) {
                int choice = preLoginHelpMessage(out, scanner);
                switch (choice) {
                    case 1:
                        continue;
                    case 2:
                        register(out, scanner);
                        break;
                    case 3:
                        loggedIn = login(out, scanner);
                        break;
                    case 4:
                        System.exit(0);
                    default:
                        out.println("Please enter a number between 1 and 4");
                        break;
                }
                out.println();
            }
            out.println("made it here");
//            while (loggedIn) {
//                loggedIn = postLoginLoop(out, scanner);
//            }
        }
    }

    private static int preLoginHelpMessage(PrintStream out, Scanner scanner) {
        out.print("""
                Enter the number next to the option you wish to select:
                1. Help
                2. Register new combatant
                3. Login returning combatant
                4. Exit the kingdom
                """);
        int choice = 0;
        try {
            choice = scanner.nextInt();
            assert 1 <= choice && choice <= 4;
        } catch (Exception e) {
            out.println("Please enter a number between 1 and 4");
        }
        return choice;
    }

    private static void register(PrintStream out, Scanner scanner) {
        out.print("""
                Hail, new combatant! Please enter registration information in this format:
                    username password email
                """);
        String input = scanner.nextLine();
        out.println(input + ": need to implement this API");
    }

    private static boolean login(PrintStream out, Scanner scanner) {
        out.print("""
                I am overjoyed to see you again! First, let me make sure your papers are in order.
                Please enter login information in this format:
                    username password
                """);
        String input = scanner.nextLine();
        out.println(input + ": need to implement this API");
        return true;
    }

    private static void callDrawChessBoard() {
        ChessGame game =  new ChessGame();
        ChessBoard board = game.getBoard();
        board.resetBoard();
        DrawChessBoard.drawBoard(board, game.getTeamTurn() == ChessGame.TeamColor.WHITE);
    }
}
