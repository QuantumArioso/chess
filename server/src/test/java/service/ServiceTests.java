package service;


import dataaccess.DataAccessException;
import db.MockedDB;
import handler.RegisterRequest;
import handler.RegisterResult;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    UserService userService;
    GameService gameService;
    AuthService authService;
    RegisterRequest registerRequest;

    @BeforeEach
    public void setup() {
        userService = new UserService();
        registerRequest = new RegisterRequest("ari", "stars8", "ari@gmail.com");
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
        RegisterResult result = userService.register(registerRequest);

        assertEquals(registerRequest.username(), result.username());
        assertTrue(MockedDB.allUserData.contains(new UserData("ari", "stars8", "ari@gmail.com")));
    }

    @Test
    @DisplayName("User: Failed Register")
    public void failureRegister() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("ari", "stars8", "ari@gmail.com");
        userService.register(registerRequest);

        assertThrows(DataAccessException.class, () -> userService.register(registerRequest));
    }

    @Test
    @DisplayName("User: Success Clear User Data")
    public void testClearUserData() {
        UserService.clearUserData();

        assertTrue(MockedDB.allUserData.isEmpty());
    }


    // Game Services

    @Test
    @DisplayName("Game: Success Clear Game Data")
    public void testClearGameData() {
        GameService.clearGameData();

        assertTrue(MockedDB.allGameData.isEmpty());
    }


    // Auth Services

    @Test
    @DisplayName("Auth: Success Clear Auth Data")
    public void testClearAuthData() {
        AuthService.clearAuthData();

        assertTrue(MockedDB.allAuthData.isEmpty());
    }
}
