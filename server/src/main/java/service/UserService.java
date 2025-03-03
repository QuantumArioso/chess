package service;

import dataaccess.*;
import handler.*;
import model.AuthData;
import model.UserData;

public class UserService {
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        // if the user already exists, send back a 403 error that the user already exists
        // I'm going to throw exceptions when this doesn't work
        if (userDAO.getUser(username) != null) {
            throw new DataAccessException("Error: already taken");
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
            throw new DataAccessException("Error: unauthorized");
        }

        if (!validatePassword(userData, loginRequest.password())) {
            throw new DataAccessException("Error: unauthorized");
        }

        AuthData authData = authDAO.createAuth(username);

        return new LoginResult(username, authData.authToken());
    }

    private boolean validatePassword(UserData userData, String password) {
        return userData.password().equals(password);
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        AuthDAO authDAO = new MemoryAuthDAO();
        String authToken = logoutRequest.authToken();
        validateAuth(authToken);

        authDAO.deleteAuthData(authToken);
    }

    public static void validateAuth(String authToken) throws DataAccessException {
        AuthDAO authDAO = new MemoryAuthDAO();
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public static void clearUserData() {
        UserDAO userDAO = new MemoryUserDAO();
        userDAO.deleteAllUserData();
    }
}
