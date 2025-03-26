package ui;

import chess.ChessBoard;
import chess.ChessGame;
import client.ClientCommunicator;
import client.ServerFacade;
import exceptions.BadRequestException;
import exceptions.UnavailableException;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    // depends on DrawChessBoard
    // depends on ServerFacade
    public static void main(String args[]) {
        ServerFacade facade = new ServerFacade(8080);
//        try {
//            facade.register("raine", "viola", "rainestorm@gmail.com");
//        } catch (BadRequestException | IOException e) {
//            System.out.println("Please enter a username that only contains letters or numbers");
//        } catch (UnavailableException e) {
//            System.out.println("There is already a user with that name. Please choose a different name");
//        }
//
//        try {
//            facade.clear();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


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
                        exit(out);
                    default:
                        out.println("Please enter a number between 1 and 4");
                }
                out.println();
            }
            out.println("""
                Your papers seem to be in order!
                """);
            out.println();
            while (loggedIn) {
                int choice = postLoginHelpMessage(out, scanner);
                switch (choice) {
                    case 1:
                        continue;
                    case 2:
                        loggedIn = logout(out);
                        break;
                    case 3:
                        createGame(out, scanner);
                        break;
                    case 4:
                        listGames(out);
                        break;
                    case 5:
                        joinGame(out, scanner);
                        break;
                    case 6:
                        observeGame(out, scanner);
                        break;
                    case 7:
                        exit(out);
                    default:
                        out.println("Please enter a number between 1 and 7");
                }
            }
            out.println();
        }
    }

    private static int preLoginHelpMessage(PrintStream out, Scanner scanner) {
        out.println("""
                Enter the number next to the option you wish to select:
                1. Help
                2. Register new combatant
                3. Login returning combatant
                4. Exit the kingdom
                """);
        int choice = 0;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
            assert 1 <= choice && choice <= 4;
        } catch (Exception e) {
            out.println("Please enter a number between 1 and 4");
        }
        return choice;
    }

    private static void register(PrintStream out, Scanner scanner) {
        out.println("""
                Hail, new combatant! Please enter registration information in this format:
                    username password email
                """);
        String input = scanner.nextLine();
        out.println(input + ": need to implement this API");
    }

    private static boolean login(PrintStream out, Scanner scanner) {
        out.println("""
                I am overjoyed to see you again! First, let me make sure your papers are in order.
                Please enter login information in this format:
                    username password
                """);
        String input = scanner.nextLine();
        out.println(input + ": need to implement this API");
        return true;
    }

    private static void exit(PrintStream out) {
        out.println("I bid thee farewell. Safe travels!");
        System.exit(0);
    }

    private static int postLoginHelpMessage(PrintStream out, Scanner scanner) {
        out.println("""
                Enter the number next to the option you wish to select:
                1. Help
                2. Logout
                3. Create a new chess game
                4. List all chess games
                5. Play a chess game
                6. Observe a chess game
                7. Exit
                """);
        int choice = 0;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
            assert 1 <= choice && choice <= 7;
        } catch (Exception e) {
            out.println("Please enter a number between 1 and 7");
        }
        return choice;
    }
    private static boolean logout(PrintStream out) {
        out.println("""
                Are you really leaving so soon?
                """);
        out.println("need to implement this API");
        return false;
    }

    private static void createGame(PrintStream out, Scanner scanner) {
        out.println("""
                Create a new game of chess! Please enter a game name:
                """);
        String input = scanner.nextLine();
        out.println(input + ": need to implement this API");
    }

    private static void listGames(PrintStream out) {
        out.println("""
                These are all the existing games
                """);
        out.println("need to implement this API");
    }

    private static void joinGame(PrintStream out, Scanner scanner) {
        out.println("""
                Enter the game number you wish to join, as well as which team you want to join as (white or black).
                Please enter information in this format:
                    number team
                """);
        String input = scanner.nextLine();
        out.println(input + ": need to implement this API");
    }

    private static void observeGame(PrintStream out, Scanner scanner) {
        out.println("""
                Enter the game number you wish to observe:
                """);
        String input = scanner.nextLine();
        out.println(input + ": need to implement this API");
    }

    private static void callDrawChessBoard() {
        ChessGame game =  new ChessGame();
        ChessBoard board = game.getBoard();
        board.resetBoard();
        DrawChessBoard.drawBoard(board, game.getTeamTurn() == ChessGame.TeamColor.WHITE);
    }
}
