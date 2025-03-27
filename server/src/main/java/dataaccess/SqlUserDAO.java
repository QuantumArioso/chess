package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Properties;

import static dataaccess.DatabaseManager.getConnection;

public class SqlUserDAO extends SqlDAO implements UserDAO {

    @Override
    public UserData getUser(String username, String password) {
        try (Connection conn = DatabaseManager.getConnection()){
            return findUser(conn, username, password);
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserData findUser(Connection conn, String username, String password) {
        var statement = "SELECT username, password, email FROM user WHERE username=? ";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, username);
            try (var results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    var dbHashedPassword = results.getString("password");
                    var dbEmail = results.getString("email");
                    if (verifyUser(dbHashedPassword, password)) {
                        return new UserData(username, password, dbEmail);
                    }
                }
            }
        }
        catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public void createUser(UserData userData) throws SQLException, DataAccessException {
        String username = userData.username();
        String hashedPassword = hashUserPassword(userData.password());
        String email = userData.email();
        try (Connection conn = DatabaseManager.getConnection()) {
            if (username.matches("[a-zA-Z]+")) {
                var statement = "INSERT INTO user (username, password, email) VALUES(?, ?, ?)";
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, hashedPassword);
                    preparedStatement.setString(3, email);
                    preparedStatement.executeUpdate();
                } catch (SQLIntegrityConstraintViolationException e) {
                    throw new UnavailableException();
                }
            } else {
                throw new BadRequestException();
            }
        }
    }

    private String hashUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    private boolean verifyUser(String hashedPassword, String providedClearTextPassword) {
        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }

    @Override
    public void deleteAllUserData() throws DataAccessException, SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE TABLE user";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }
    }
}
