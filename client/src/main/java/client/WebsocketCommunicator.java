package client;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exceptions.BadRequestException;
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketCommunicator extends Endpoint {
    Session session;
    ServerMessageObserver serverMessageObserver;


    public WebsocketCommunicator(String url, ServerMessageObserver notificationHandler) throws BadRequestException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageObserver = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    if (message.contains("LOAD_GAME")) {
                        LoadGameMessage loadGame = new Gson().fromJson(message, LoadGameMessage.class);
                        notificationHandler.notify(loadGame);
                    } else if (message.contains("ERROR")) {
                        ErrorMessage error = new Gson().fromJson(message, ErrorMessage.class);
                        notificationHandler.notify(error);
                    } else if (message.contains("NOTIFICATION")) {
                        NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                        notificationHandler.notify(notification);
                    } else {
                        ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                        notificationHandler.notify(notification);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new BadRequestException();
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID) {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new BadRequestException();
        }
    }

    public void move(String authToken, int gameID, ChessMove move) {
        try {
            var action = new MakeMoveCommand(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new BadRequestException();
        }
    }

    public void leave(String authToken, int gameID, ChessGame.TeamColor teamColor) {
        try {
            var action = new LeaveGameCommand(authToken, gameID, teamColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new BadRequestException();
        }
    }

    public void resign(String authToken, int gameID) {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new BadRequestException();
        }
    }
}
