package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.UnauthorizedException;
import model.AuthData;

public class AuthService {
    public static void validateAuth(String authToken) throws UnauthorizedException {
        AuthDAO authDAO = new MemoryAuthDAO();
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException();
        }
    }

    public static void clearAuthData() {
        AuthDAO authDAO = new MemoryAuthDAO();
        try {
            authDAO.deleteAllAuthData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
