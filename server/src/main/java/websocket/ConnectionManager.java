package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Connection, Integer> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session, Integer gameID) {
        var connection = new Connection(visitorName, session);
        connections.put(connection, gameID);
    }

    public void remove(String visitorName) {
        for (Connection connection : connections.keySet()) {
            String conn = connection.visitorName;
            if (conn.equals(visitorName)) {
                connections.remove(connection);
            }
        }

    }

    public void broadcast(String excludeVisitorName, Integer gameID, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (Connection connection : connections.keySet()) {
            if (connection.session.isOpen()) {
                if (Objects.equals(connections.get(connection), gameID) && !connection.visitorName.equals(excludeVisitorName)) {
                    connection.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(connection);
            }
        }

        // Clean up any connections that were left open.
        for (var connection : removeList) {
            connections.remove(connection);
        }
    }

    public void send(String username, Integer gameID, ServerMessage notification) throws IOException {
        for (Connection connection : connections.keySet()) {
            if (connection.session.isOpen()) {
                if (Objects.equals(connections.get(connection), gameID) && connection.visitorName.equals(username)) {
                    connection.send(new Gson().toJson(notification));
                }
            }
        }
    }
}
