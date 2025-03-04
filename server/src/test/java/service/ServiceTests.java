package service;


import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import dataaccess.UnauthorizedException;
import dataaccess.UnavailableException;
import db.MockedDB;
import handler.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    UserService userService;
    GameService gameService;
    AuthService authService;
    RegisterRequest registerRequest;
    LoginRequest loginRequest;
    String authToken;
    GameCreateRequest gameCreateRequest;
    int gameID;

    @BeforeEach
    public void setup() throws DataAccessException {
        userService = new UserService();
        gameService = new GameService();
        authService = new AuthService();
        registerRequest = new RegisterRequest("ari", "stars8", "ari@gmail.com");
        userService.register(registerRequest);
        loginRequest = new LoginRequest("ari", "stars8");
        authToken = userService.login(loginRequest).authToken();
        gameCreateRequest = new GameCreateRequest(authToken, "Test Game");
        gameID = gameService.createGame(gameCreateRequest).gameID();
    }

    @AfterEach
    public void clear() {
        UserService.clearUserData();
        GameService.clearGameData();
        AuthService.clearAuthData();
    }


    // User Services

    @Test
    @DisplayName("User: Success Register")
    public void successRegister() throws DataAccessException {
        RegisterRequest newRequest = new RegisterRequest("raine", "whispers", "bard_magic@gmail.com");
        RegisterResult result = userService.register(newRequest);

        assertEquals(newRequest.username(), result.username());
        assertTrue(MockedDB.allUserData.contains(new UserData("raine", "whispers", "bard_magic@gmail.com")));
    }

    @Test
    @DisplayName("User: Failed Register")
    public void failureRegister() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userService.register(registerRequest));
    }

    @Test
    @DisplayName("User: Success Login")
    public void successLogin() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest("ari", "stars8");
        LoginResult result = userService.login(loginRequest);

        assertEquals(loginRequest.username(), result.username());
    }

    @Test
    @DisplayName("User: Failed Login")
    public void failureLogin() throws DataAccessException {
        // Incorrect username
        LoginRequest loginRequest = new LoginRequest("ar", "stars8");
        assertThrows(DataAccessException.class, () -> userService.login(loginRequest));

        // Incorrect password
        LoginRequest loginRequest1 = new LoginRequest("ari", "tars8");
        assertThrows(DataAccessException.class, () -> userService.login(loginRequest1));
    }

    @Test
    @DisplayName("User: Success Logout")
    public void successLogout() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest("ari", "stars8");
        LoginResult loginResult = userService.login(loginRequest);
        String authToken = loginResult.authToken();

        userService.logout(new LogoutRequest(authToken));

        for (AuthData data : MockedDB.allAuthData) {
            assertNotEquals(authToken, data.authToken());
        }
    }

    @Test
    @DisplayName("User: Failed Logout")
    public void failureLogout() throws DataAccessException {
        String badAuthToken = "hello_world";
        LogoutRequest request = new LogoutRequest(badAuthToken);
        assertThrows(DataAccessException.class, () -> userService.logout(request));
    }

    @Test
    @DisplayName("User: Success Clear User Data")
    public void testClearUserData() {
        UserService.clearUserData();

        assertTrue(MockedDB.allUserData.isEmpty());
    }


    // Game Services

    @Test
    @DisplayName("Game: Success Create Game")
    public void successCreateGame() throws DataAccessException {
        String authToken = userService.login(new LoginRequest("ari", "stars8")).authToken();
        GameCreateResult result = gameService.createGame(new GameCreateRequest(authToken, "My Game"));

        boolean inDatabase = false;
        for (GameData data : MockedDB.allGameData) {
            if (data.gameName().equals("My Game")) {
                inDatabase = true;
                break;
            }
        }
        assertTrue(inDatabase);
        assertEquals(MemoryGameDAO.gameCounter - 1, result.gameID());
    }

    @Test
    @DisplayName("Game: Failed Create Game")
    public void failedCreateGame() {
        String badAuthToken = "hello_world";
        GameCreateRequest request = new GameCreateRequest(badAuthToken, "My Game");
        assertThrows(DataAccessException.class, () -> gameService.createGame(request));
    }

    @Test
    @DisplayName("Game: Success Join Game")
    public void successJoinGame() throws DataAccessException {
        String authToken = userService.login(new LoginRequest("ari", "stars8")).authToken();
        GameCreateResult createResult = gameService.createGame(new GameCreateRequest(authToken, "My Game"));
        GameJoinRequest request = new GameJoinRequest(authToken, ChessGame.TeamColor.BLACK, createResult.gameID());
        gameService.joinGame(request);

        boolean inDatabase = false;
        for (GameData data : MockedDB.allGameData) {
            if (data.gameID() == request.gameID()) {
                inDatabase = "ari".equals(data.blackUsername());
                break;
            }
        }
        assertTrue(inDatabase);
    }

    @Test
    @DisplayName("Game: Failed Join Game Bad Auth")
    public void failedJoinGameBadAuth() {
        String badAuthToken = "hello_world";
        GameJoinRequest request1 = new GameJoinRequest(badAuthToken, ChessGame.TeamColor.WHITE, gameID);
        assertThrows(UnauthorizedException.class, () -> gameService.joinGame(request1));
    }

    @Test
    @DisplayName("Game: Failed Join Game Player Already In Use")
    public void failedJoinGamePlayerInUse() throws DataAccessException {
        GameJoinRequest request1 = new GameJoinRequest(authToken, ChessGame.TeamColor.BLACK, gameID);
        gameService.joinGame(request1);
        GameJoinRequest request2 = new GameJoinRequest(authToken, ChessGame.TeamColor.BLACK, gameID);
        assertThrows(UnavailableException.class, () -> gameService.joinGame(request2));
    }

    @Test
    @DisplayName("Game: Success List Games")
    public void successListGames() throws DataAccessException {
        GameCreateRequest newGame = new GameCreateRequest(authToken, "New Game");
        gameService.createGame(newGame);

        GameListRequest request = new GameListRequest(authToken);
        gameService.listGames(request);

        assertEquals(2, MockedDB.allGameData.size());
    }

    @Test
    @DisplayName("Game: Failed List Games")
    public void failedListGames() {
        String badAuthToken = "hello_world";
        GameListRequest request = new GameListRequest(badAuthToken);
        assertThrows(UnauthorizedException.class, () -> gameService.listGames(request));
    }

    @Test
    @DisplayName("Game: Success Clear Game Data")
    public void testClearGameData() {
        GameService.clearGameData();

        assertTrue(MockedDB.allGameData.isEmpty());
    }


    // Auth Services

    @Test
    @DisplayName("Validate Auth")
    public void testValidateAuth() {
        String badAuthToken = "Bad Auth Token";
        assertThrows(UnauthorizedException.class, () -> AuthService.validateAuth(badAuthToken));
    }

    @Test
    @DisplayName("Auth: Success Clear Auth Data")
    public void testClearAuthData() {
        AuthService.clearAuthData();

        assertTrue(MockedDB.allAuthData.isEmpty());
    }
}
