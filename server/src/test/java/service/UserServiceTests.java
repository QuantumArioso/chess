package service;


import dataaccess.DataAccessException;
import db.MockedDB;
import handler.RegisterRequest;
import handler.RegisterResult;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    UserService service;
    RegisterRequest registerRequest;

    @BeforeEach
    public void setup() {
        service = new UserService();
        registerRequest = new RegisterRequest("ari", "stars8", "ari@gmail.com");
    }

    @AfterEach
    public void clear() {
        service.clearUserData();
    }


    @Test
    public void successRegister() throws DataAccessException {
        RegisterResult result = service.register(registerRequest);

        assertEquals(registerRequest.username(), result.username());
        assertTrue(MockedDB.allUserData.contains(new UserData("ari", "stars8", "ari@gmail.com")));
    }

    @Test
    public void failureRegister() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("ari", "stars8", "ari@gmail.com");
        service.register(registerRequest);

        assertThrows(DataAccessException.class, () -> service.register(registerRequest));
    }

    @Test
    public void testClearUserData() {
        service.clearUserData();

        assertTrue(MockedDB.allUserData.isEmpty());
    }
}
