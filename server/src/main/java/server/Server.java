package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import handler.EmptyResult;
import handler.Handler;
import handler.RegisterRequest;
import handler.RegisterResult;
import spark.*;

import java.util.Map;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::registerBody); //register

        Spark.delete("/db", this::clearBody);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    // turns body into a Map object that I can use in Java
    private Object registerBody(Request req, Response res) throws DataAccessException {
        Map<String, String> body = convertFromJSON(req);
        RegisterRequest request = new RegisterRequest(body.get("username"), body.get("password"), body.get("email"));
        RegisterResult result = Handler.register(request);

        res.status(200);
        res.type("application/json");
        return new Gson().toJson(result);
    }

    private Object clearBody(Request req, Response res) {
        EmptyResult empty = Handler.clearDatabase();
        res.status(200);
        Spark.exception(Exception.class, this::errorHandler);
        return new Gson().toJson(empty);
    }

    private static Map<String, String> convertFromJSON(Request request) {
        var body = new Gson().fromJson(request.body(), Map.class);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    private Object errorHandler(Exception e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
