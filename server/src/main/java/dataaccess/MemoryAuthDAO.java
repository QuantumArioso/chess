package dataaccess;

import db.MockedDB;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO {
    public AuthData createAuth(String username) {
        String authToken = AuthData.generateAuthToken();
        AuthData authData = new AuthData(authToken, username);
        MockedDB.allAuthData.add(authData);
        return authData;
    }

    public AuthData getAuth(String authToken) {
        for (AuthData data : MockedDB.allAuthData) {
            if (data.authToken().equals(authToken)) {
                return data;
            }
        }
        return null;
    }

    @Override
    public void deleteAuthData(String authToken) {
        MockedDB.allAuthData.removeIf(data -> authToken.equals(data.authToken()));
    }

    public void deleteAllAuthData() {
        MockedDB.allAuthData = new ArrayList<>();
    }
}
