package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import client.ServerFacade;
import client.ServerMessageObserver;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import exceptions.UnavailableException;
import model.GameData;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ui.EscapeSequences.*;

public class Client implements ServerMessageObserver {
    static ServerFacade facade = new ServerFacade(8080);

    public void run() {
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

    private static void gameplayLoop(PrintStream out, Scanner scanner, ChessGame game, ChessGame.TeamColor teamColor,
                                     String authToken, int gameID) {
        boolean leave = false;
        while (!leave) {
            int choice = gameplayHelpMessage(out, scanner);
            switch (choice) {
                case 1:
                    break;
                case 2:
                    boolean needToFlip = teamColor == null || teamColor.equals(ChessGame.TeamColor.WHITE);
                    redrawChessBoard(needToFlip, game, null, game.getBoard());
                    break;
                case 3:
                    makeMove();
                    //TODO: implement this function
                    break;
                case 4:
                    highlightLegalMoves(out, scanner, game, teamColor);
                    break;
                case 5:
                    leave = true;
                    facade.leaveGame(authToken, gameID);
                    break;
                case 6:
                    resignFromGame();
                    //TODO: implement this function
                    break;
                default:
                    out.println("Please enter a number between 1 and 6");
            }
        }
    }

    private static int gameplayHelpMessage(PrintStream out, Scanner scanner) {
        out.println("""
                Enter the number next to the option you wish to select:
                1. Help
                2. Redraw chess board
                3. Make a move
                4. Highlight legal moves
                5. Leave the game
                6. Resign from the game
                """);
        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.nextLine());
            assert 1 <= choice && choice <= 6;
        } catch (Exception e) {
            out.println("Please enter a number between 1 and 6");
        }
        return choice;
    }

    private static void makeMove() {

    }

    private static void highlightLegalMoves(PrintStream out, Scanner scanner, ChessGame game,
                                            ChessGame.TeamColor teamColor) {
        out.println("""
                Please enter the coordinates of the piece who's moves you'd like to see in this format: e5
                """);
        String[] input = scanner.nextLine().split("");

        Map<String, Integer> coordinates = new HashMap<>();
        coordinates.put("a", 1);
        coordinates.put("b", 2);
        coordinates.put("c", 3);
        coordinates.put("d", 4);
        coordinates.put("e", 5);
        coordinates.put("f", 6);
        coordinates.put("g", 7);
        coordinates.put("h", 8);

        Set<String> keys = coordinates.keySet();
        Collection<Integer> values = coordinates.values();
        int row;
        try {
            row = Integer.parseInt(input[1]);
        } catch (Exception e) {
            out.println("Please enter a valid coordinate position");
            return;
        }
        if (!keys.contains(input[0]) || !values.contains(row)) {
            out.println("Please enter a valid coordinate position");
            return;
        }
        int col = coordinates.get(input[0]);
        ChessPosition pos = new ChessPosition(row, col);
        if (game.getBoard().getPiece(pos) == null) {
            out.println("There is not a piece at that location");
            return;
        }
        boolean needToFlip = teamColor == null || teamColor.equals(ChessGame.TeamColor.WHITE);
        redrawChessBoard(needToFlip, game, pos, game.getBoard());
    }

    private static void resignFromGame() {

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
            ArrayList<GameData> list = facade.listGames(authToken);
            String output = "";
            for (int i = 0; i < list.size(); i++) {
                GameData gameData = list.get(i);
                int gameID = gameData.gameID();
                String gameName = gameData.gameName();
                String blackUsername = gameData.blackUsername();
                if (blackUsername == null) {
                    blackUsername = "none";
                }
                Object whiteUsername = gameData.whiteUsername();
                if (whiteUsername == null) {
                    whiteUsername = "none";
                }
                output += "Game ID: " + gameID + "   Game Name: " + gameName + "   Black Player: " + blackUsername +
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
            ArrayList<GameData> games = facade.listGames(authToken);
            if (games.isEmpty()) {
                out.println("There are no games to join. Please create a game first.");
                return;
            }

            getGameIds(gameIDs, games);
            int gameID = Integer.parseInt(input[0]);
            if (!gameIDs.contains(gameID)) {
                out.printf("Please enter a number between 1 and %d to join a game, or create a new game " +
                        "for more options.\n", gameIDs.size());
                return;
            }
            facade.joinGame(authToken, Double.parseDouble(input[0]), uppercaseTeamColor);
            int index = (gameID) - 1;
            GameData gameData = games.get(index);
            out.println("You have joined the game!");
            initialDrawChessBoard(uppercaseTeamColor.equals("WHITE"), gameData.game(), null);
            ChessGame.TeamColor teamColor;
            if (uppercaseTeamColor.equals("WHITE")) {
                teamColor = ChessGame.TeamColor.WHITE;
            } else {
                teamColor = ChessGame.TeamColor.BLACK;
            }
            gameplayLoop(out, scanner, gameData.game(), teamColor, authToken, gameID);
        } catch (UnavailableException e) {
            out.println("That color is already taken. Please try the other color or join as an observer.");
        } catch (IOException e) {
            out.println("Something went wrong. Please try again");
        }
    }

    private static void getGameIds(ArrayList<Integer> gameIDs, ArrayList<GameData> list) {
        for (int i = 0; i < list.size(); i++) {
            GameData gameData = list.get(i);
            int gameID = gameData.gameID();
            gameIDs.add(gameID);
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
            ArrayList<GameData> games = facade.listGames(authToken);
            if (games.isEmpty()) {
                out.println("There are no games to observe.");
                return;
            }
            int gameID = Integer.parseInt(input);
            ArrayList<Integer> gameIDs = new ArrayList<>();
            getGameIds(gameIDs, games);
            if (!gameIDs.contains(gameID)) {
                out.printf("Please enter a number between 1 and %d to observe a game.\n", gameIDs.size());
                return;
            }
            int index = (Integer.parseInt(input)) - 1;
            GameData gameData = games.get(index);
            facade.observeGame(authToken, gameID);
            initialDrawChessBoard(true, gameData.game(), null);
            gameplayLoop(out, scanner, gameData.game(), null, authToken, gameID);
        } catch (IOException e) {
            out.println("Something went wrong. Please try again");
        }

    }

    private static void initialDrawChessBoard(boolean needToFlip, ChessGame game, ChessPosition pos) {
        ChessBoard board = game.getBoard();
        board.resetBoard();
        DrawChessBoard.drawBoard(game, needToFlip, pos);
    }

    private static void redrawChessBoard(boolean needToFlip, ChessGame game, ChessPosition pos, ChessBoard newBoard) {
        game.setBoard(newBoard);
        DrawChessBoard.drawBoard(game, needToFlip, pos);
    }

    @Override
    public void notify(ServerMessage message) {
        if(message.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
            notificationMessageReceived((NotificationMessage) message);
        } else if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
            loadGameMessageReceived((LoadGameMessage) message);
        } else if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
            errorMessageReceived((ErrorMessage) message);
        }
    }

    public void notificationMessageReceived(NotificationMessage notification) {
        System.out.println(SET_TEXT_COLOR_YELLOW + notification.getMessage());
        System.out.println(RESET_TEXT_COLOR);
    }

    public void loadGameMessageReceived(LoadGameMessage load) {
        ChessGame game = load.getGame();
        redrawChessBoard(//how do I know who I'm displaying it for...);
    }

    public void errorMessageReceived(ErrorMessage error) {

    }
}
