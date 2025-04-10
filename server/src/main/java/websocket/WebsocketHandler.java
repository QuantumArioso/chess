package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.SqlGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.LeaveGameCommand;
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
    private final SqlAuthDAO sqlAuthDAO = new SqlAuthDAO();
    private final SqlGameDAO sqlGameDAO = new SqlGameDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.

            String username = sqlAuthDAO.getAuth(command.getAuthToken()).username();
            GameData gameData = sqlGameDAO.getGameData(command.getGameID());
            int gameID = gameData.gameID(); // throws an exception if invalid gameID

            if (command.getCommandType().equals(UserGameCommand.CommandType.LEAVE)) {
                if (username.equals(gameData.blackUsername())) {
                    command = new LeaveGameCommand(command.getAuthToken(), command.getGameID(), ChessGame.TeamColor.BLACK);
                } else if (username.equals(gameData.whiteUsername())) {
                    command = new LeaveGameCommand(command.getAuthToken(), command.getGameID(), ChessGame.TeamColor.WHITE);
                } else {
                    command = new LeaveGameCommand(command.getAuthToken(), command.getGameID(), null);
                }
            }

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
//                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
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

    private void connect(Session session, String username, UserGameCommand command) throws IOException {
        connections.add(username, session);
        LoadGameMessage notification = new LoadGameMessage(command.getGameID());
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
    private void leaveGame(Session session, String username, LeaveGameCommand command) throws IOException {
        connections.remove(username);
        if (command.getTeamColor() != null) {
            sqlGameDAO.updateGamePlayer(command.getGameID(), command.getTeamColor(), null);
        }

        String message = String.format("%s has left the game", username);
        var notification2 = new NotificationMessage(message);
        connections.broadcast(username, notification2);
    }
//
//    private void resign(Session session, String username, ResignCommand command) {
//
//    }

}
