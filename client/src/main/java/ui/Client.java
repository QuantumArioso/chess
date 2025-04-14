package ui;
import client.ServerMessageObserver;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import static ui.EscapeSequences.*;

public class Client implements ServerMessageObserver {
    ClientHelper helper = new ClientHelper();

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
                int choice = helper.preLoginHelpMessage(out, scanner);
                switch (choice) {
                    case 1:
                        continue;
                    case 2:
                        helper.register(out, scanner);
                        break;
                    case 3:
                        authToken = helper.login(out, scanner);
                        break;
                    case 4:
                        helper.exit(out);
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
                int choice = helper.postLoginHelpMessage(out, scanner);
                switch (choice) {
                    case 1:
                        continue;
                    case 2:
                        authToken = helper.logout(out, authToken);
                        break;
                    case 3:
                        helper.createGame(out, scanner, authToken);
                        break;
                    case 4:
                        helper.listGames(out, authToken);
                        break;
                    case 5:
                        helper.joinGame(out, scanner, authToken);
                        break;
                    case 6:
                        helper.observeGame(out, scanner, authToken);
                        break;
                    case 7:
                        helper.exit(out);
                    default:
                        out.println("Please enter a number between 1 and 7");
                }
            }
            out.println();
        }
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
        helper.redrawChessBoard(load.isNeedToFlip(), load.getGame(), null, load.getGame().getBoard());
    }

    public void errorMessageReceived(ErrorMessage error) {
        System.out.println(SET_TEXT_COLOR_RED + error.getErrorMessage());
        System.out.println(RESET_TEXT_COLOR);
    }
}
