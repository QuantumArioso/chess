package client;

import exceptions.UnavailableException;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() throws IOException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        ServerFacade.register("raine", "888", "rainestorm@gmail.com");
    }

    @AfterAll
    static void stopServer() throws IOException {
        server.stop();
        ServerFacade.clear();
    }

    @Test
    @DisplayName("Register User Success")
    public void createUserPositive() throws IOException {
        assertEquals(36, ServerFacade.register("amity", "abomination8", "mittens@gmail.com").length());
    }

    @Test
    @DisplayName("Register User Already Exists")
    public void createUserNegative() throws IOException {
        assertThrows(UnavailableException.class, () -> ServerFacade.register("raine", "888", "rainestorm@gmail.com"));
    }

    @Test
    @DisplayName("Clear All Data")
    public void clearTest() throws IOException {
        ServerFacade.clear();
        // if this works, I can successfully add the exact same user as before
        assertEquals(36, ServerFacade.register("raine", "888", "rainestorm@gmail.com").length());
    }
}
