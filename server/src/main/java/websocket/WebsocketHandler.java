package websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.SqlGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveCommand;
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

            String username = sqlAuthDAO.getAuth(command.getAuthToken()).username();
            GameData gameData = sqlGameDAO.getGameData(command.getGameID());
            int gameID = gameData.gameID(); // throws an exception if invalid gameID
            ChessGame.TeamColor teamColor = getTeamColor(username, gameData);

            if (command.getCommandType().equals(UserGameCommand.CommandType.LEAVE)) {
                command = new LeaveGameCommand(command.getAuthToken(), command.getGameID(), teamColor);
            } else if (msg.contains("move")) {
                MakeMoveCommand command2 = new Gson().fromJson(msg, MakeMoveCommand.class);
                makeMove(session, username, command2, gameData);
            }

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command, teamColor);
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
                case RESIGN -> resign(session, username, command, teamColor);
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

    private void connect(Session session, String username, UserGameCommand command, ChessGame.TeamColor teamColor) throws IOException {
        connections.add(username, session, command.getGameID());
        ChessGame game = sqlGameDAO.getGameData(command.getGameID()).game();

        LoadGameMessage notification;
        if (teamColor != null && teamColor.equals(ChessGame.TeamColor.BLACK)) {
            notification = new LoadGameMessage(game, false);
        } else {
            notification = new LoadGameMessage(game, true);
        }
        String notif = new Gson().toJson(notification);
        session.getRemote().sendString(notif);

        String message;
        if (teamColor == null) {
            message = String.format("%s has joined the game as an observer", username);
        } else {
            message = String.format("%s has joined the game as the %s team", username, teamColor);
        }
        var notification2 = new NotificationMessage(message);
        connections.broadcast(username, command.getGameID(), notification2);
    }

    private void makeMove(Session session, String username, MakeMoveCommand command, GameData gameData) throws IOException {
        ChessMove move = command.getMove();
        ChessGame game = sqlGameDAO.getGameData(command.getGameID()).game();
        ChessGame.TeamColor teamColor = getTeamColor(username, gameData);
        ChessGame.TeamColor teamTurn = game.getTeamTurn();
        if (game.getGameOver()) {
            String message = "Error: The game is over. You cannot make a move";
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage(message)));
            return;
        }
        if (!teamTurn.equals(teamColor)) {
            String message = "Error: It is not your turn. You cannot make a move";
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage(message)));
            return;
        }
        try {
            if (teamColor.equals(game.getBoard().getPiece(move.getStartPosition()).getTeamColor())) {
                game.makeMove(move);
                sqlGameDAO.updateChessGame(new Gson().toJson(game), gameData.gameID());

                if (teamColor.equals(ChessGame.TeamColor.BLACK)) {
                    session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(game, false)));
                    LoadGameMessage notification = new LoadGameMessage(game, true);
                    connections.broadcast(username, command.getGameID(), notification);
                } else {
                    String blackUsername = gameData.blackUsername();
                    LoadGameMessage notification = new LoadGameMessage(game, false);
                    connections.send(blackUsername, gameData.gameID(), notification);
                    notification = new LoadGameMessage(game, true);
                    connections.broadcast(blackUsername, command.getGameID(), notification);
                }


            } else {
                String message = "Error: You cannot move that piece";
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage(message)));
                return;
            }
        } catch (InvalidMoveException e) {
            String errorMessage = new Gson().toJson(new ErrorMessage("Error: " + e.getMessage()));
            session.getRemote().sendString(errorMessage);
            return;
        }

        ChessPosition pos = move.getEndPosition();
        ChessPiece piece = game.getBoard().getPiece(pos);
        String message = String.format("%s has moved %s to %s", username, piece, pos);
        var notif = new NotificationMessage(message);
        connections.broadcast(username, command.getGameID(), notif);

        if (game.isInStalemate(teamColor)) {
            message = "You are in stalemate";
            session.getRemote().sendString(new Gson().toJson(new NotificationMessage(message)));

            message = String.format("%s is in stalemate", username);
            connections.broadcast(username, command.getGameID(), new NotificationMessage(message));
        } else if (game.isInCheckmate(teamColor)) {
            message = "You are in checkmate";
            session.getRemote().sendString(new Gson().toJson(new NotificationMessage(message)));

            message = String.format("%s is in checkmate", username);
            connections.broadcast(username, command.getGameID(), new NotificationMessage(message));
        } else if (game.isInCheck(teamColor)) {
            message = "You are in check";
            session.getRemote().sendString(new Gson().toJson(new NotificationMessage(message)));

            message = String.format("%s is in check", username);
            connections.broadcast(username, command.getGameID(), new NotificationMessage(message));
        }
    }

    private void leaveGame(Session session, String username, LeaveGameCommand command) throws IOException {
        connections.remove(username);
        if (command.getTeamColor() != null) {
            sqlGameDAO.updateGamePlayer(command.getGameID(), command.getTeamColor(), null);
        }

        String message = String.format("%s has left the game", username);
        var notification2 = new NotificationMessage(message);
        connections.broadcast(username, command.getGameID(), notification2);
    }

    private void resign(Session session, String username, UserGameCommand command, ChessGame.TeamColor teamColor) throws IOException {
        ChessGame game = sqlGameDAO.getGameData(command.getGameID()).game();
        if (game.getGameOver()) {
            String message = "Error: The game is already over";
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage(message)));
            return;
        }
        if (teamColor == null) {
            String message = "Error: You are an observer. You cannot resign";
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage(message)));
            return;
        }
        game.setGameOver(true);
        sqlGameDAO.updateChessGame(new Gson().toJson(game), command.getGameID());
        String message = String.format("%s has resigned from the game", username);
        var notification = new NotificationMessage(message);
        connections.broadcast("", command.getGameID(), notification);
    }

    private ChessGame.TeamColor getTeamColor(String username, GameData gameData) {
        if (username.equals(gameData.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        } else if (username.equals(gameData.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        } else {
            return null;
        }
    }
}
