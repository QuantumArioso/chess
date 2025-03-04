package handler;


import dataaccess.DataAccessException;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Handler {
    public static EmptyResult clearDatabase() {
        UserService.clearUserData();
        GameService.clearGameData();
        AuthService.clearAuthData();

        return new EmptyResult();
    }

    public static RegisterResult register(RegisterRequest request) throws DataAccessException {
        UserService service = new UserService();
        return service.register(request);
    }

    public static LoginResult login(LoginRequest request) throws DataAccessException {
        UserService service = new UserService();
        return service.login(request);
    }
}
