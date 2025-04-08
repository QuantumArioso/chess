package client;

import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import exceptions.UnavailableException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    static String authToken;

    @BeforeAll
    public static void init() throws IOException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    void setup() throws IOException {
        facade.clear();
        authToken = facade.register("raine", "888", "rainestorm@gmail.com");
        facade.createGame(authToken, "the boiling isles");
    }

    @AfterAll
    static void stopServer() throws IOException {
        server.stop();
    }

    @Test
    @DisplayName("Register Success")
    public void createUserPositive() throws IOException {
        assertEquals(36, facade.register("amity", "abomination8", "mittens@gmail.com").length());
    }

    @Test
    @DisplayName("Register User Already Exists")
    public void createUserNegative() {
        assertThrows(UnavailableException.class, () -> facade.register("raine", "888", "rainestorm@gmail.com"));
    }

    @Test
    @DisplayName("Login Success")
    public void loginPositive() throws IOException {
        assertEquals(36, facade.login("raine", "888").length());
    }

    @Test
    @DisplayName("Login Wrong Password")
    public void loginNegative() {
        assertThrows(UnauthorizedException.class, () -> facade.login("raine", "222"));
    }

    @Test
    @DisplayName("Logout Success")
    public void logoutPositive() throws IOException {
        facade.logout(authToken);
        assertThrows(UnauthorizedException.class, () -> facade.createGame(authToken, "new game"));
    }

    @Test
    @DisplayName("Logout Bad authToken")
    public void logoutNegative() {
        assertThrows(UnauthorizedException.class, () -> facade.logout("bad authToken"));
    }

    @Test
    @DisplayName("Create Game Success")
    public void createGamePositive() throws IOException {
        assertEquals(2.0, facade.createGame(authToken, "new game"));
    }

    @Test
    @DisplayName("Create Game Bad authToken")
    public void createGameNegative() {
        assertThrows(UnauthorizedException.class, () -> facade.createGame("bad authToken", "new game"));
    }

    @Test
    @DisplayName("List Games Success")
    public void listGamesPositive() throws IOException {
        facade.createGame(authToken, "hexside");
        facade.createGame(authToken, "the owl house");
        assertEquals(3, facade.listGames(authToken).size());
    }

    @Test
    @DisplayName("List Games Bad authToken")
    public void listGamesNegative() {
        assertThrows(UnauthorizedException.class, () -> facade.listGames("bad authToken"));
    }

    @Test
    @DisplayName("Join Game Success")
    public void joinGamePositive() throws IOException {
        facade.joinGame(authToken, 1.0, "BLACK");
        ArrayList<GameData> games = facade.listGames(authToken);
        assertEquals(1, games.size());
        assertEquals("raine", games.getFirst().blackUsername());
    }

    @Test
    @DisplayName("Join Game Invalid authToken")
    public void joinGameNegative() {
        assertThrows(UnauthorizedException.class, () -> facade.joinGame("bad authToken", 1.0, "WHITE"));
    }

    @Test
    @DisplayName("Clear All Data")
    public void clearTest() throws IOException {
        facade.clear();
        // if this works, I can successfully add the exact same user as before
        assertEquals(36, facade.register("raine", "888", "rainestorm@gmail.com").length());
    }
}
