package client;

import exceptions.UnavailableException;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;

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
    public void createUserNegative() throws IOException {
        assertThrows(UnavailableException.class, () -> facade.register("raine", "888", "rainestorm@gmail.com"));
    }

    //TODO: Why is this giving me a 500? Also need a negative test
    @Test
    @DisplayName("Login Success")
    public void loginPositive() throws IOException {
        assertEquals(36, facade.login("raine", "888").length());
    }

    @Test
    @DisplayName("Create Game Success")
    public void createGamePositive() throws IOException {
        assertEquals(1.0, facade.createGame(authToken, "new game"));
    }

    @Test
    @DisplayName("Clear All Data")
    public void clearTest() throws IOException {
        facade.clear();
        // if this works, I can successfully add the exact same user as before
        assertEquals(36, facade.register("raine", "888", "rainestorm@gmail.com").length());
    }
}
