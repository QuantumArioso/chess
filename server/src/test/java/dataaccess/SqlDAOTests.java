package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SqlDAOTests {
    Connection conn;
    UserDAO userDAO;
    UserData userData;

    @BeforeEach
    void setup() throws DataAccessException, SQLException {
        conn = DatabaseManager.getConnection();
        userDAO = new SqlUserDAO();

        userData = new UserData("rainewhispers", "viola", "bard@gmail.com");
        userDAO.createUser(userData);
    }

    @AfterEach
    void teardown() throws SQLException, DataAccessException {
        userDAO.deleteAllUserData();
    }

    @Test
    @DisplayName("Get User Success")
    void testGetUserPositive() {
        UserData returnedUserData = userDAO.getUser(userData.username(), userData.password());
        assertEquals(userData.username(), returnedUserData.username());
        assertEquals(userData.password(), returnedUserData.password());
    }

    @Test
    @DisplayName("Get User Invalid Password")
    void testGetUserNegative() {
        UserData returnedUserData = userDAO.getUser(userData.username(), "wrong");
        assertNull(returnedUserData);
    }

    @Test
    @DisplayName("Create User Success")
    void testCreateUserPositive() throws DataAccessException, SQLException {
        UserData userData = new UserData("arioso", "hello8", "ari@gmail.com");
        userDAO.createUser(userData);
        var conn = DatabaseManager.getConnection();

        var statement = "SELECT username FROM user WHERE username=? ";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, "arioso");
            try (var results = preparedStatement.executeQuery()) {
                assertTrue(results.next());
            }
        }

    }

    @Test
    @DisplayName("Create User User Already Exists")
    void testCreateUserNegative() throws SQLException, DataAccessException {
        UserData userData = new UserData("rainewhispers", "viola", "bard@gmail.com");
        assertThrows(UnavailableException.class, () -> userDAO.createUser(userData));
    }

    @Test
    @DisplayName("Delete all user data")
    void testDeleteAllUserData() throws SQLException, DataAccessException {
        userDAO.deleteAllUserData();
        var statement = "SELECT username FROM user WHERE username=? ";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, "rainewhispers");
            try (var results = preparedStatement.executeQuery()) {
                assertFalse(results.next());
            }
        }
    }
}
