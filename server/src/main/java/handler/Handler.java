package handler;


import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import service.AuthService;
import service.GameService;
import service.UserService;

import java.sql.SQLException;

public class Handler {
    public static EmptyResult clearDatabase() {
        UserService.clearUserData();
        GameService.clearGameData();
        AuthService.clearAuthData();

        return new EmptyResult();
    }

    public static RegisterResult register(RegisterRequest request) throws DataAccessException, SQLException {
        UserService service = new UserService();
        return service.register(request);
    }

    public static LoginResult login(LoginRequest request) throws DataAccessException {
        UserService service = new UserService();
        return service.login(request);
    }

    public static EmptyResult logout(LogoutRequest request) throws DataAccessException {
        UserService service = new UserService();
        return service.logout(request);
    }

    public static GameListResult listGames(GameListRequest request) throws UnauthorizedException {
        GameService service = new GameService();
        return service.listGames(request);
    }

    public static GameCreateResult createGame(GameCreateRequest request) throws DataAccessException {
        GameService service = new GameService();
        return service.createGame(request);
    }

    public static EmptyResult joinGame(GameJoinRequest request) throws DataAccessException {
        GameService service = new GameService();
        return service.joinGame(request);
    }
}
