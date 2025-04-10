package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlAuthDAO extends SqlDAO implements AuthDAO {
    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = AuthData.generateAuthToken();
        try (Connection conn = DatabaseManager.getConnection()) {
            if (username.matches("[a-zA-Z0-9]+")) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData getAuth(String authToken) {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT authToken, username FROM auth WHERE authToken=? ";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try (var results = preparedStatement.executeQuery()) {
                    while (results.next()) {
                        var dbUsername = results.getString("username");
                        return new AuthData(authToken, dbUsername);
                    }
                }
            }
            catch (SQLException e) {
                return null;
            }
            return null;
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAuthData(String authToken) {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "DELETE FROM auth WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAllAuthData() throws DataAccessException, SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE TABLE auth";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }
    }
}
