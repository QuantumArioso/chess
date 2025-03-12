package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlAuthDAO extends SqlDAO implements AuthDAO {
    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = AuthData.generateAuthToken();
        Connection conn = DatabaseManager.getConnection();

        if (username.matches("[a-zA-Z]+")) {
            var statement = "INSERT INTO auth (authToken, username) VALUES(?, ?)";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return new AuthData(authToken, username);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public AuthData getAuth(String authToken) {
        throw new RuntimeException("Not implemented");
    }

    public void deleteAuthData(String authToken) {
        throw new RuntimeException("Not implemented");
    }

    public void deleteAllAuthData() throws DataAccessException, SQLException {
        Connection conn = DatabaseManager.getConnection();
        var statement = "TRUNCATE TABLE auth";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        }
    }
}
