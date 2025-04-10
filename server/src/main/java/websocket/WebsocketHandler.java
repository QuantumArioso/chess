package websocket;

import com.google.gson.Gson;
import dataaccess.SqlGameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

import dataaccess.UnauthorizedException;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

import dataaccess.SqlAuthDAO;

@WebSocket
public class WebsocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            SqlAuthDAO sqlAuthDAO = new SqlAuthDAO();
            SqlGameDAO sqlGameDAO = new SqlGameDAO();
            String username = sqlAuthDAO.getAuth(command.getAuthToken()).username();
            int gameID = sqlGameDAO.getGameData(command.getGameID()).gameID();

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, gameID);
//                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
//                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
//        } catch (UnauthorizedException ex) {
//            // Serializes and sends the error message
//            String errorMessage = new Gson().toJson(new ErrorMessage("Error: unauthorized"));
//            session.getRemote().sendString(errorMessage);
        } catch (Exception ex) {
            String errorMessage = new Gson().toJson(new ErrorMessage("Error: " + ex.getMessage()));
            session.getRemote().sendString(errorMessage);
        }
    }

    private void connect(Session session, String username, int gameID) throws IOException {
        connections.add(username, session);
        LoadGameMessage notification = new LoadGameMessage(gameID);
        String notif = new Gson().toJson(notification);
        session.getRemote().sendString(notif);

        String message = String.format("%s has joined the game", username);
        var notification2 = new NotificationMessage(message);
        connections.broadcast(username, notification2);
    }
//
//    private void makeMove(Session session, String username, MakeMoveCommand command) {
//
//    }
//
//    private void leaveGame(Session session, String username, LeaveGameCommand command) {
//
//    }
//
//    private void resign(Session session, String username, ResignCommand command) {
//
//    }

}
