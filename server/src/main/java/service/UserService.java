package service;

import dataaccess.*;
import handler.*;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;

public class UserService {
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, SQLException {
        String username = registerRequest.username();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();

        if (userDAO.getUser(username) != null) {
            throw new UnavailableException();
        }

        UserData userData = new UserData(username, registerRequest.password(), registerRequest.email());
        userDAO.createUser(userData);
        AuthData authData = authDAO.createAuth(username);

        return new RegisterResult(username, authData.authToken());
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        String username = loginRequest.username();

        UserData userData = userDAO.getUser(username);
        if (userData == null) {
            throw new UnauthorizedException();
        }

        if (!validatePassword(userData, loginRequest.password())) {
            throw new UnauthorizedException();
        }

        AuthData authData = authDAO.createAuth(username);

        return new LoginResult(username, authData.authToken());
    }

    private boolean validatePassword(UserData userData, String password) {
        return userData.password().equals(password);
    }

    public EmptyResult logout(LogoutRequest logoutRequest) throws DataAccessException {
        AuthDAO authDAO = new MemoryAuthDAO();
        String authToken = logoutRequest.authToken();
        AuthService.validateAuth(authToken);

        authDAO.deleteAuthData(authToken);

        return new EmptyResult();
    }

    public static void clearUserData() throws SQLException, DataAccessException {
        UserDAO userDAO = new MemoryUserDAO();
        userDAO.deleteAllUserData();
    }
}
