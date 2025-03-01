package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;

public class AuthService {
    public static void clearAuthData() {
        AuthDAO authDAO = new MemoryAuthDAO();
        authDAO.deleteAllAuthData();
    }
}
