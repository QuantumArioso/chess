package service;

import dataaccess.*;
import model.AuthData;

public class AuthService {
    public static void validateAuth(String authToken) throws UnauthorizedException {
        AuthDAO authDAO = new SqlAuthDAO();
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException();
        }
    }

    public static void clearAuthData() {
        AuthDAO authDAO = new SqlAuthDAO();
        try {
            authDAO.deleteAllAuthData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
