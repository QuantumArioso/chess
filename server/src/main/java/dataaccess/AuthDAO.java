package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {
    AuthData createAuth(String username) throws DataAccessException, SQLException;

    AuthData getAuth(String authToken);

    void deleteAuthData(String authToken);

    void deleteAllAuthData() throws DataAccessException, SQLException;
}
