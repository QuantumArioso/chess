package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import handler.*;
import spark.*;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class Server {
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        new SqlDAO();

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::registerBody); //register
        Spark.post("/session", this::loginBody); //login
        Spark.post("/game", this::createGameBody); //create game

        Spark.get("/game", this::listGamesBody); //list games

        Spark.put("game", this::joinGameBody); //join game

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void badRegisterRequest(Map<String, String> body) throws BadRequestException {
        if (!body.containsKey("username") || !body.containsKey("password") || !body.containsKey("email") ||
                body.get("username").isEmpty() || body.get("password").isEmpty() || body.get("email").isEmpty()) {
            throw new BadRequestException();
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

    private Object listGamesBody(Request req, Response res) throws DataAccessException {
        try {
            GameListRequest request = new GameListRequest(req.headers("authorization"));
            GameListResult result = Handler.listGames(request);

            res.status(200);
            res.type("application/json");
            return new Gson().toJson(result);
        }
        catch (UnauthorizedException e) {
            return errorHandler(e.getMessage(), req, res, 401);
        }
    }

    private Object createGameBody(Request req, Response res) throws DataAccessException {
        try {
            Map<String, String> body = convertFromJSON(req);
            GameCreateRequest request = new GameCreateRequest(req.headers("authorization"), body.get("gameName"));
            GameCreateResult result = Handler.createGame(request);

            res.status(200);
            res.type("application/json");
            return new Gson().toJson(result);
        } catch (BadRequestException e) {
            return errorHandler(e.getMessage(), req, res, 400);
        }
        catch (UnauthorizedException e) {
            return errorHandler(e.getMessage(), req, res, 401);
        }
    }

    private Object joinGameBody(Request req, Response res) throws DataAccessException {
        try {
            Map<String, String> body = convertFromJSON(req);

            ChessGame.TeamColor playerColor = getPlayerColor(body.get("playerColor"));

            if (body.get("gameID") == null) {
                throw new BadRequestException();
            }
            double doubleGameID = Double.parseDouble(body.get("gameID"));
            int gameID = (int) doubleGameID;

            GameJoinRequest request = new GameJoinRequest(req.headers("authorization"), playerColor, gameID);
            EmptyResult result = Handler.joinGame(request);

            res.status(200);
            res.type("application/json");
            return new Gson().toJson(result);
        }
        catch (BadRequestException e) {
            return errorHandler(e.getMessage(), req, res, 400);
        }
        catch (UnauthorizedException e) {
            return errorHandler(e.getMessage(), req, res, 401);
        }
        catch (UnavailableException e) {
            return errorHandler(e.getMessage(), req, res, 403);
        }
    }

    private ChessGame.TeamColor getPlayerColor(String strPlayerColor) {
        if (strPlayerColor != null && strPlayerColor.contains("W")) {
            return ChessGame.TeamColor.WHITE;
        } else if (strPlayerColor != null && strPlayerColor.contains("B")) {
            return ChessGame.TeamColor.BLACK;
        } else {
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
        Set<String> keys = body.keySet();
        for (String key : keys) {
            if (body.get(key) instanceof Double) {
                body.put(key, String.valueOf(body.get(key)));
            }
        }
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
