package dataaccess;

import db.MockedDB;
import model.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO {
    public AuthData createAuth(String username) {
        String authToken = AuthData.generateAuthToken();
        AuthData authData = new AuthData(authToken, username);
        MockedDB.allAuthData.add(authData);
        return authData;
    }

    public AuthData getAuth(String authToken) {
        throw new RuntimeException("Not implemented");
    }

    public void deleteAllAuthData() {
        MockedDB.allAuthData = new ArrayList<>();
    }
}
