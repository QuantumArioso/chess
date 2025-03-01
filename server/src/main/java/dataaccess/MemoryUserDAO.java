package dataaccess;

import db.MockedDB;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;


public class MemoryUserDAO implements UserDAO {
    public UserData getUser(String username) {
        for (UserData data : MockedDB.allUserData) {
            if (data.username().equals(username)) {
                return data;
            }
        }
        return null;
    }

    public void createUser(UserData userData) {
        MockedDB.allUserData.add(userData);
    }

    @Override
    public void deleteAllUserData() {
        MockedDB.allUserData = new ArrayList<>();
    }
}
