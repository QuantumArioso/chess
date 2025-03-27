package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SqlDAOTests {
    Connection conn;
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserData userData;

    @BeforeEach
    void setup() throws DataAccessException, SQLException {
        conn = DatabaseManager.getConnection();
        userDAO = new SqlUserDAO();
        authDAO = new SqlAuthDAO();
        gameDAO = new SqlGameDAO();

        userData = new UserData("rainewhispers", "viola", "bard@gmail.com");
        userDAO.createUser(userData);
    }

    @AfterEach
    void teardown() throws SQLException, DataAccessException {
        userDAO.deleteAllUserData();
        authDAO.deleteAllAuthData();
        gameDAO.deleteAllGameData();
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

    @Test
    @DisplayName("Create Auth Success")
    void testCreateAuthPositive() throws SQLException, DataAccessException {
        authDAO.createAuth("rainewhispers");

        var statement = "SELECT username FROM auth WHERE username=? ";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, "rainewhispers");
            try (var results = preparedStatement.executeQuery()) {
                assertTrue(results.next());
            }
        }
    }

    @Test
    @DisplayName("Create Auth Invalid Username")
    void testCreateAuthNegative() {
        assertThrows(UnauthorizedException.class, () -> authDAO.createAuth("raine}; --DROP"));
    }

    @Test
    @DisplayName("Get Auth Data Success")
    void testGetAuthPositive() throws SQLException, DataAccessException {
        String authToken = authDAO.createAuth("rainewhispers").authToken();
        AuthData authData = authDAO.getAuth(authToken);

        assertEquals(authToken, authData.authToken());
        assertEquals("rainewhispers", authData.username());
    }

    @Test
    @DisplayName("Get Auth Data Invalid authToken")
    void testGetAuthNegative() throws SQLException, DataAccessException {
        assertNull(authDAO.getAuth("not an authToken"));
    }

    @Test
    @DisplayName("Delete Auth Data Success")
    void testDeleteAuthPositive() throws SQLException, DataAccessException {
        String authToken = authDAO.createAuth("rainewhispers").authToken();
        authDAO.deleteAuthData(authToken);

        var statement = "SELECT authToken FROM auth WHERE authToken=? ";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, authToken);
            try (var results = preparedStatement.executeQuery()) {
                assertFalse(results.next());
            }
        }
    }

    @Test
    @DisplayName("Delete all auth data")
    void testDeleteAllAuthData() throws SQLException, DataAccessException {
        authDAO.deleteAllAuthData();
        var statement = "SELECT username FROM auth WHERE username=? ";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, "rainewhispers");
            try (var results = preparedStatement.executeQuery()) {
                assertFalse(results.next());
            }
        }
    }

    @Test
    @DisplayName("Add new game success")
    void testAddNewGamePositive() throws SQLException {
        String gameName = "New Game";
        GameData gameData = gameDAO.addNewGame(gameName);

        var statement = "SELECT gameID, gameName FROM game WHERE gameName=? ";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, gameName);
            try (var results = preparedStatement.executeQuery()) {
                boolean bool = results.next();
                assertTrue(bool);
                while (bool) {
                    int dbGameID = results.getInt("gameID");
                    assertEquals(gameData.gameID(), dbGameID);
                    bool = results.next();
                }
            }
        }
    }

    @Test
    @DisplayName("Add new game invalid game name")
    void testAddNewGameNegative() {
        assertNull(gameDAO.addNewGame(""));
    }

    @Test
    @DisplayName("Get game data success")
    void testGetGameDataPositive() {
        GameData firstGameData = gameDAO.addNewGame("New Game");
        GameData gameData = gameDAO.getGameData(firstGameData.gameID());

        ChessGame game = gameData.game();
        assertEquals(ChessGame.TeamColor.WHITE, game.getTeamTurn());
    }

    @Test
    @DisplayName("Get game invalid gameID")
    void testGetGameDataNegative() {
        assertNull(gameDAO.getGameData(-1));
    }

    @Test
    @DisplayName("Update game player success")
    void testUpdateGamePlayerPositive() {
        GameData firstGameData = gameDAO.addNewGame("New Game");
        GameData gameData = gameDAO.updateGamePlayer(firstGameData.gameID(), ChessGame.TeamColor.BLACK, "rainewhispers");
        assertEquals("rainewhispers", gameData.blackUsername());
    }

    @Test
    @DisplayName("Update game player invalid gameID")
    void testUpdateGamePlayerNegative() {
        gameDAO.addNewGame("New Game");
        assertNull(gameDAO.updateGamePlayer(-1, ChessGame.TeamColor.BLACK, "rainewhispers"));
    }

    @Test
    @DisplayName("Get all game data success")
    void testGetAllGameDataPositive() {
        ArrayList<GameData> expectedGameData = new ArrayList<>();
        expectedGameData.add(gameDAO.addNewGame("New Game"));
        expectedGameData.add(gameDAO.addNewGame("New Game"));
        expectedGameData.add(gameDAO.addNewGame("The Boiling Isles"));
        expectedGameData.add(gameDAO.addNewGame("Hexside"));

        ArrayList<GameData> actualGameData = SqlGameDAO.getAllGameData();
        assertEquals(expectedGameData.size(), actualGameData.size());
        for (int i = 0; i < actualGameData.size(); i++) {
            // idk if my games are actually equal...
            assertEquals(expectedGameData.get(i).gameID(), actualGameData.get(i).gameID());
            assertEquals(expectedGameData.get(i).gameName(), actualGameData.get(i).gameName());
        }
    }

    @Test
    @DisplayName("Get all game data no game data")
    void testGetAllGameDataNegative() throws SQLException, DataAccessException {
        gameDAO.deleteAllGameData();
        ArrayList<GameData> expectedGameData = new ArrayList<>();
        ArrayList<GameData> actualGameData = SqlGameDAO.getAllGameData();
        assertEquals(expectedGameData.size(), actualGameData.size());
    }

    @Test
    @DisplayName("Delete all game data")
    void testDeleteAllGameData() throws SQLException, DataAccessException {
        gameDAO.deleteAllGameData();
        var statement = "SELECT gameID FROM game WHERE gameID=? ";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, "1");
            try (var results = preparedStatement.executeQuery()) {
                assertFalse(results.next());
            }
        }
    }
}
