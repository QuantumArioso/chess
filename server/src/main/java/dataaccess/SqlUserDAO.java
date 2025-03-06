package dataaccess;

import model.UserData;

public class SqlUserDAO extends SqlDAO implements UserDAO {
    @Override
    public UserData getUser(String username) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void createUser(UserData userData) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteAllUserData() {
        throw new RuntimeException("Not implemented");
    }
}
