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

    public LoginResult login(LoginRequest loginRequest) {
        throw new RuntimeException("Not implemented");
    }

    public void logout(LogoutRequest logoutRequest) {
        throw new RuntimeException("Not implemented");
    }

    public static void clearUserData() {
        UserDAO userDAO = new MemoryUserDAO();
        userDAO.deleteAllUserData();
    }
}
