package client;

import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
    @DisplayName("Clear All Data")
    public void clearTest() throws IOException {
        ServerFacade.clear();
        // if this works, I can successfully add the exact same user as before
        assertEquals(36, ServerFacade.register("raine", "888", "rainestorm@gmail.com").length());
    }

    @Test
    @DisplayName("Create User Success")
    public void createUserPositive() {

    }
}
