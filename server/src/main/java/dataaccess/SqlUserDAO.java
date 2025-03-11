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
    public UserData getUser(String username) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void createUser(UserData userData) throws SQLException, DataAccessException {
        String username = userData.username();
        String hashedPassword = hashUserPassword(userData.password());
        String email = userData.email();
        Connection conn = DatabaseManager.getConnection();

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
        }
    }

    private String hashUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    @Override
    public void deleteAllUserData() throws DataAccessException, SQLException {
        Connection conn = DatabaseManager.getConnection();
        var statement = "TRUNCATE TABLE user";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        }
    }

//    private boolean verifyUser(String username, String providedClearTextPassword) {
//        // read the previously hashed password from the database
//        var hashedPassword = readHashedPasswordFromDatabase(username);
//
//        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
//    }
}
