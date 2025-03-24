package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;


public class ServerFacade {
    // has 7 methods--one for each request
    // depends on ClientCommunicator for get and post

    public static void main(String[] args) throws Exception {

        String data = """
                {
                    "username": "ag",
                    "password": "abomination8",
                    "email": "amityblight@gmail.com"
                }
                """;
        String result = ClientCommunicator.doPost("http://localhost:8080/user", data);
        System.out.println(result);


        // Specify the desired endpoint
//        URI uri = new URI("http://localhost:8080/name");
//        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
//        http.setRequestMethod("GET");
//
//        // Make the request
//        http.connect();
//
//
//        // Output the response body
//        try (InputStream respBody = http.getInputStream()) {
//            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
//            System.out.println(new Gson().fromJson(inputStreamReader, Map.class));
//        }
    }
}
