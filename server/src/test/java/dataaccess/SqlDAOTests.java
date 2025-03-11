package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SqlDAOTests {
    Connection conn;
    UserDAO userDAO;

    @BeforeEach
    void setup() throws DataAccessException, SQLException {
        conn = DatabaseManager.getConnection();
        userDAO = new SqlUserDAO();

        UserData userData = new UserData("raine", "whispers", "bard@gmail.com");
        userDAO.createUser(userData);

        userDAO.deleteAllUserData();
    }

    @Test
    @DisplayName("Create User Success")
    void testCreateUserPositive() throws DataAccessException, SQLException {
        UserData userData = new UserData("ari", "hello8", "ari@gmail.com");
        userDAO.createUser(userData);
        var conn = DatabaseManager.getConnection();

        var statement = "SELECT username FROM user WHERE username=? ";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, "ari");
            try (var results = preparedStatement.executeQuery()) {
                assertTrue(results.next());
            }
        }

    }

    @Test
    @DisplayName("Create User User Already Exists")
    void testCreateUserNegative() throws SQLException, DataAccessException {
        UserData userData = new UserData("ari", "hello8", "ari@gmail.com");
        userDAO.createUser(userData);
        assertThrows(UnavailableException.class, () -> userDAO.createUser(userData));
    }

    @Test
    @DisplayName("Delete all user data")
    void testDeleteAllUserData() throws SQLException, DataAccessException {
        userDAO.deleteAllUserData();
        var statement = "SELECT username FROM user WHERE username=? ";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, "raine");
            try (var results = preparedStatement.executeQuery()) {
                assertFalse(results.next());
            }
        }
    }
}
