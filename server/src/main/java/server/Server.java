package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import db.MockedDB;
import handler.EmptyResult;
import handler.Handler;
import handler.RegisterRequest;
import handler.RegisterResult;
import model.UserData;
import spark.*;

import java.util.Map;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::registerBody); //register

        Spark.delete("/db", this::clearBody); //clear

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    // turns body into a Map object that I can use in Java
    private Object registerBody(Request req, Response res) throws DataAccessException {
        Map<String, String> body = convertFromJSON(req);
        if (!body.containsKey("username") || !body.containsKey("password") || !body.containsKey("email") ||
                body.get("username").isEmpty() || body.get("password").isEmpty() || body.get("email").isEmpty()) {
            return errorHandler("bad request", req, res, 400);
        }
        try {
            RegisterRequest request = new RegisterRequest(body.get("username"), body.get("password"), body.get("email"));
            RegisterResult result = Handler.register(request);
            res.status(200);
            res.type("application/json");
            return new Gson().toJson(result);

        } catch (DataAccessException e) {
            return errorHandler(e.getMessage(), req, res, 403);
        }
    }

    private Object clearBody(Request req, Response res) {
        EmptyResult empty = Handler.clearDatabase();
        res.status(200);
        return new Gson().toJson(empty);
    }

    private static Map<String, String> convertFromJSON(Request request) {
        var body = new Gson().fromJson(request.body(), Map.class);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    private Object errorHandler(String message, Request req, Response res, int statusCode) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", message), "success", false));
        res.type("application/json");
        res.status(statusCode);
        res.body(body);
        return body;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
