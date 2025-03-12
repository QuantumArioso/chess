package service;

import dataaccess.*;
import handler.*;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;

public class UserService {
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, SQLException {
        String username = registerRequest.username();
        String password = registerRequest.password();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new SqlUserDAO();

        if (userDAO.getUser(username, password) != null) {
            throw new UnavailableException();
        }

        UserData userData = new UserData(username, registerRequest.password(), registerRequest.email());
        userDAO.createUser(userData);
        AuthData authData = authDAO.createAuth(username);

        return new RegisterResult(username, authData.authToken());
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserDAO userDAO = new SqlUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        String username = loginRequest.username();
        String password = loginRequest.password();

        UserData userData = userDAO.getUser(username, password);
        if (userData == null) {
            throw new UnauthorizedException();
        }

        if (!validatePassword(userData, loginRequest.password())) {
            throw new UnauthorizedException();
        }

        try {
            AuthData authData = authDAO.createAuth(username);
            return new LoginResult(username, authData.authToken());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        UserDAO userDAO = new SqlUserDAO();
        userDAO.deleteAllUserData();
    }
}
