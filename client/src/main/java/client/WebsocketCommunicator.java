package client;

import com.google.gson.Gson;
import exceptions.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;


import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketCommunicator extends Endpoint {
    private static final Logger log = LoggerFactory.getLogger(WebsocketCommunicator.class);
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
                    NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            log.error(String.valueOf(ex));
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
            log.error(ex.getMessage());
            throw new BadRequestException();
        }
    }
}
