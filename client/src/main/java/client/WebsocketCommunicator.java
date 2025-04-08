package client;

import com.google.gson.Gson;
import exceptions.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


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
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
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
}
