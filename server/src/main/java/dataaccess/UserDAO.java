package dataaccess;

import model.UserData;

import java.sql.SQLException;

public interface UserDAO {
    UserData getUser(String username);

    void createUser(UserData userData) throws SQLException;

    void deleteAllUserData();
}
