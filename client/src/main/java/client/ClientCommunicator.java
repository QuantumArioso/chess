package client;

import com.google.gson.Gson;
import exceptions.BadRequestException;
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

    public static String doPost(String urlString, String data) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set HTTP request headers, if necessary
        // connection.addRequestProperty("Accept", "text/html");

        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream();) {
            // Write request body to OutputStream ...
//            var jsonBody = new Gson().toJson(data);
            requestBody.write(data.getBytes());
        }

        int responseCode = connection.getResponseCode();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary
            // Map<String, List<String>> headers = connection.getHeaderFields();

            // OR

            //connection.getHeaderField("Content-Length");

            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            Map body = new Gson().fromJson(reader, Map.class);
            String jsonBody = new Gson().toJson(body);
            return jsonBody;
            // Read response body from InputStream ...
        }
        else {
            // SERVER RETURNED AN HTTP ERROR

            InputStream responseBody = connection.getErrorStream();
            System.out.println(responseBody);

            if (responseCode == 400) {
                throw new BadRequestException();
            } else if (responseCode == 403) {
                throw new UnavailableException();
            }
            // Read and process error response body from InputStream ...
        }
        return "";
    }

    public static void doGet(String urlString, String authToken) throws IOException {
        System.out.println("in doGet");
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        // Set HTTP request headers, if necessary
        // connection.addRequestProperty("Accept", "text/html");
         connection.addRequestProperty("Authorization", authToken);

        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary
            // Map<String, List<String>> headers = connection.getHeaderFields();

            // OR

            //connection.getHeaderField("Content-Length");

            InputStream responseBody = connection.getInputStream();
            System.out.println("success: " + responseBody);
        } else {
            // SERVER RETURNED AN HTTP ERROR

            InputStream responseBody = connection.getErrorStream();
            System.out.println("error: " + responseBody);
            // Read and process error response body from InputStream ...
        }
    }
}
