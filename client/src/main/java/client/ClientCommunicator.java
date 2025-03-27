package client;

import com.google.gson.Gson;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import exceptions.UnavailableException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ClientCommunicator {
    // where the get and post code goes
    // the thing that actually knows about HTTP

    public static String doPost(String urlString, String data, String authToken) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        if (!authToken.isEmpty()) {
            connection.addRequestProperty("authorization", authToken);
        }

        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream();) {
            requestBody.write(data.getBytes());
        }

        int responseCode = connection.getResponseCode();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return convertToJson(connection.getInputStream());
        }
        else {
            if (responseCode == 400) {
                throw new BadRequestException();
            } else if (responseCode == 401) {
                throw new UnauthorizedException();
            }
            else if (responseCode == 403) {
                throw new UnavailableException();
            }
        }
        return "";
    }

    private static String convertToJson(InputStream responseBody) {
        InputStreamReader reader = new InputStreamReader(responseBody);
        Map body = new Gson().fromJson(reader, Map.class);
        return new Gson().toJson(body);
    }

    public static String doGet(String urlString, String authToken) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        // Set HTTP request headers, if necessary
        connection.addRequestProperty("authorization", authToken);

        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return convertToJson(connection.getInputStream());
        } else {
            int responseCode = connection.getResponseCode();
            if (responseCode == 401) {
                throw new UnauthorizedException();
            }
        }
        return "";
    }

    public static void doPut(String urlString, String data, String authToken) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        // Set HTTP request headers, if necessary
        connection.addRequestProperty("authorization", authToken);

        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream();) {
            requestBody.write(data.getBytes());
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == 400) {
            throw new BadRequestException();
        } else if (responseCode == 401) {
            throw new UnauthorizedException();
        }
        else if (responseCode == 403) {
            throw new UnavailableException();
        }
    }

    public static void doDelete(String urlString, String authToken) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("DELETE");

        if (!authToken.isEmpty()) {
            connection.addRequestProperty("authorization", authToken);
        }

        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == 401) {
            throw new UnauthorizedException();
        }
    }
}
