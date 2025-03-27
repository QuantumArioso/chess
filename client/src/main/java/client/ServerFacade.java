package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;


public class ServerFacade {
    // has 7 methods--one for each request
    // depends on ClientCommunicator for get and post
    int port;
    String url;
    public ServerFacade(int port) {
        this.port = port;
        url = String.format("http://localhost:%d", port);
    }

    public String register(String username, String password, String email) throws IOException {
        String data = String.format("""
            {
                "username": %s,
                "password": %s,
                "email": %s
            }
            """, username, password, email);
        String result = ClientCommunicator.doPost(url + "/user", data, "");
        Map body = new Gson().fromJson(result, Map.class);
        return (String) body.get("authToken");
    }

    public String login(String username, String password) throws IOException {
        String data = String.format("""
            {
                "username": %s,
                "password": %s
            }
            """, username, password);
        String result = ClientCommunicator.doPost(url + "/session", data, "");
        Map body = new Gson().fromJson(result, Map.class);
        return (String) body.get("authToken");
    }

    public void logout(String authToken) throws IOException {
        ClientCommunicator.doDelete(url + "/session", authToken);
    }

    public double createGame(String authToken, String gameName) throws IOException {
        String data = String.format("""
            {
                "gameName": "%s"
            }
            """, gameName);
        String result = ClientCommunicator.doPost(url + "/game", data, authToken);
        Map body = new Gson().fromJson(result, Map.class);
        return (double) body.get("gameID");
    }

    public ArrayList listGames(String authToken) throws IOException {
        String result = ClientCommunicator.doGet(url + "/game", authToken);
        Map<String, Object> body = new Gson().fromJson(result, Map.class);
        return (ArrayList) body.get("games");
    }

    public void joinGame(String authToken, double gameID, String playerColor) throws IOException {
        String data = String.format("""
                {
                    "playerColor": %s,
                    "gameID": %f
                }
                """, playerColor, gameID);
        ClientCommunicator.doPut(url + "/game", data, authToken);
    }

    public void clear() throws IOException {
        ClientCommunicator.doDelete(url + "/db", "");
    }
}
