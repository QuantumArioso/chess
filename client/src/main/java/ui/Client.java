package ui;

import chess.ChessBoard;
import chess.ChessGame;
import client.ClientCommunicator;
import client.ServerFacade;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import exceptions.UnavailableException;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Client {
    static ServerFacade facade = new ServerFacade(8080);

    public static void main(String args[]) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(System.in);
        out.print("""
                Welcome to my kingdom! We are in the midst of a war and could use thy assistance.
                What do ye wish to do first?
                """);
        while (true) {
            String authToken = "";
            while (authToken.isEmpty()) {
                int choice = preLoginHelpMessage(out, scanner);
                switch (choice) {
                    case 1:
                        continue;
                    case 2:
                        register(out, scanner);
                        break;
                    case 3:
                        authToken = login(out, scanner);
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
            while (!authToken.isEmpty()) {
                int choice = postLoginHelpMessage(out, scanner);
                switch (choice) {
                    case 1:
                        continue;
                    case 2:
                        authToken = logout(out, authToken);
                        break;
                    case 3:
                        createGame(out, scanner, authToken);
                        break;
                    case 4:
                        listGames(out, authToken);
                        break;
                    case 5:
                        joinGame(out, scanner, authToken);
                        break;
                    case 6:
                        observeGame(out, scanner, authToken);
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
            choice = Integer.parseInt(scanner.nextLine());
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
        String[] input = scanner.nextLine().strip().split(" ");
        if (input.length != 3) {
            out.println("Please only enter a username, password, and email");
            return;
        }
        try {
            facade.register(input[0], input[1], input[2]);
            out.println("Successfully registered!");
        } catch (BadRequestException | IOException e) {
            out.println("Please enter a username that only contains letters");
        } catch (UnavailableException e) {
            out.println("There is already a user with that name. Please choose a different name");
        }
    }

    private static String login(PrintStream out, Scanner scanner) {
        out.println("""
                I am overjoyed to see you again! First, let me make sure your papers are in order.
                Please enter login information in this format:
                    username password
                """);
        String[] input = scanner.nextLine().strip().split(" ");
        if (input.length != 2) {
            out.println("Please only enter a username and password");
            return "";
        }
        try {
            return facade.login(input[0], input[1]);
        } catch (UnauthorizedException e) {
            out.println("Your information is incorrect. Please try again");
            return "";
        } catch (IOException e) {
            out.println("That was not the correct input. Please try again");
            return "";
        }
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
            choice = Integer.parseInt(scanner.nextLine());
            assert 1 <= choice && choice <= 7;
        } catch (Exception e) {
            out.println("Please enter a number between 1 and 7");
        }
        return choice;
    }
    private static String logout(PrintStream out, String authToken) {
        out.println("""
                See you later!
                """);
        try {
            facade.logout(authToken);
            return "";
        } catch (IOException e) {
            out.println("Something went wrong. Please try again");
            return authToken;
        }
    }

    private static void createGame(PrintStream out, Scanner scanner, String authToken) {
        out.println("""
                Create a new game of chess! Please enter a game name:
                """);
        String input = scanner.nextLine().strip();
        try {
            double gameID = facade.createGame(authToken, input);
            out.println("Game " + (int) gameID + " has been created!");
        } catch (IOException e) {
            out.println("Something went wrong. Please try again");
        }
    }

    private static void listGames(PrintStream out, String authToken) {
        out.println("""
                These are all the existing games:
                """);
        try {
            ArrayList<Map> list = facade.listGames(authToken);
            String output = "";
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                Object gameID = map.get("gameID");
                double gameDouble = (double) gameID;
                int gameInt = (int) gameDouble;
                Object gameName = map.get("gameName");
                Object blackUsername = map.get("blackUsername");
                if (blackUsername == null) {
                    blackUsername = "none";
                }
                Object whiteUsername = map.get("whiteUsername");
                if (whiteUsername == null) {
                    whiteUsername = "none";
                }
                output += "Game ID: " + gameInt + "   Game Name: " + gameName + "   Black Player: " + blackUsername +
                        "   White Player: " + whiteUsername + "\n";
            }
            out.println(output);
        } catch (IOException e) {
            out.println("Something went wrong. Please try again");
        }
    }

    private static void joinGame(PrintStream out, Scanner scanner, String authToken) {
        out.println("""
                Enter the game number you wish to join, as well as which team you want to join as (white or black).
                Please enter information in this format:
                    number team
                """);
        String[] input = scanner.nextLine().strip().split(" ");
        if (input.length != 2) {
            out.println("Please only enter a number and team color");
            return;
        }
        String uppercaseTeamColor = input[1].toUpperCase();
        if (!(uppercaseTeamColor.equals("WHITE") || uppercaseTeamColor.equals("BLACK"))) {
            out.println("Please enter 'white' or 'black' to choose the team to join");
            return;
        }

        try {
            Integer.parseInt(input[0]);
        } catch (Exception e) {
            out.println("Please enter a number for the gameID");
            return;
        }

        ArrayList<Integer> gameIDs = new ArrayList<>();
        try {
            ArrayList<Map> list = facade.listGames(authToken);
            if (list.isEmpty()) {
                out.println("There are no games to join. Please create a game first.");
                return;
            }

            getGameIds(gameIDs, list);
            if (!gameIDs.contains(Integer.parseInt(input[0]))) {
                out.printf("Please enter a number between 1 and %d to join a game, or create a new game " +
                        "for more options.\n", gameIDs.size());
                return;
            }
            facade.joinGame(authToken, Double.parseDouble(input[0]), uppercaseTeamColor);
            out.println("You have joined the game!");
            callDrawChessBoard(uppercaseTeamColor.equals("WHITE"));
        } catch (UnavailableException e) {
            out.println("That color is already taken. Please try the other color or join as an observer.");
        } catch (IOException e) {
            out.println("Something went wrong. Please try again");
        }
    }

    private static void getGameIds(ArrayList<Integer> gameIDs, ArrayList<Map> list) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            Object gameID = map.get("gameID");
            double gameDouble = (double) gameID;
            gameIDs.add((int) gameDouble);
        }
    }

    private static void observeGame(PrintStream out, Scanner scanner, String authToken) {
        out.println("""
                Enter the game number you wish to observe:
                """);
        String input = scanner.nextLine().strip();

        try {
            Integer.parseInt(input);
        } catch (Exception e) {
            out.println("Please enter a valid number for the gameID");
            return;
        }

        try {
            ArrayList<Map> list = facade.listGames(authToken);
            if (list.isEmpty()) {
                out.println("There are no games to observe.");
                return;
            }
            ArrayList<Integer> gameIDs = new ArrayList<>();
            getGameIds(gameIDs, list);
            if (!gameIDs.contains(Integer.parseInt(input))) {
                out.printf("Please enter a number between 1 and %d to observe a game.\n", gameIDs.size());
                return;
            }
            callDrawChessBoard(true);
        } catch (IOException e) {
            out.println("Something went wrong. Please try again");
        }

    }

    private static void callDrawChessBoard(boolean needToFlip) {
        ChessGame game =  new ChessGame();
        ChessBoard board = game.getBoard();
        board.resetBoard();
        DrawChessBoard.drawBoard(board, needToFlip);
    }
}
