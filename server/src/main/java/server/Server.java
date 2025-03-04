package server;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.UnavailableException;
import handler.*;
import spark.*;

import java.util.Map;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::registerBody); //register
        Spark.post("/session", this::loginBody); //login

        Spark.delete("/session", this::logoutBody); //logout
        Spark.delete("/db", this::clearBody); //clear

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object registerBody(Request req, Response res) throws DataAccessException {
        Map<String, String> body = convertFromJSON(req);
        try {
            badRegisterRequest(body);

            RegisterRequest request = new RegisterRequest(body.get("username"), body.get("password"), body.get("email"));
            RegisterResult result = Handler.register(request);

            res.status(200);
            res.type("application/json");
            return new Gson().toJson(result);
        }
        catch (UnavailableException e) {
            return errorHandler(e.getMessage(), req, res, 403);
        }
        catch (BadRequestException e) {
            return errorHandler(e.getMessage(), req, res, 400);
        }
    }

    private Object loginBody(Request req, Response res) throws DataAccessException {
        Map<String, String> body = convertFromJSON(req);
        try {
            LoginRequest request = new LoginRequest(body.get("username"), body.get("password"));
            LoginResult result = Handler.login(request);

            res.status(200);
            res.type("application/json");
            return new Gson().toJson(result);
        }
        catch (UnauthorizedException e) {
            return errorHandler(e.getMessage(), req, res, 401);
        }
    }

    private Object logoutBody(Request req, Response res) throws DataAccessException {
        try {
            LogoutRequest request = new LogoutRequest(req.headers("authorization"));
            EmptyResult result = Handler.logout(request);

            res.status(200);
            res.type("application/json");
            return new Gson().toJson(result);
        }
        catch (UnauthorizedException e) {
            return errorHandler(e.getMessage(), req, res, 401);
        }
    }

    private void badRegisterRequest(Map<String, String> body) throws BadRequestException {
        if (!body.containsKey("username") || !body.containsKey("password") || !body.containsKey("email") ||
                body.get("username").isEmpty() || body.get("password").isEmpty() || body.get("email").isEmpty()) {
            throw new BadRequestException();
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
