package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;


public class ServerFacade {
    // has 7 methods--one for each request
    // depends on ClientCommunicator for get and post

    public static String register(String username, String password, String email) throws IOException {
        String data = String.format("""
            {
                "username": %s,
                "password": %s,
                "email": %s
            }
            """, username, password, email);
        String result = ClientCommunicator.doPost("http://localhost:8080/user", data);
        Map body = new Gson().fromJson(result, Map.class);
        return (String) body.get("authToken");
    }

    public static void clear() throws IOException {
        ClientCommunicator.doDelete("http://localhost:8080/db");
    }
}
