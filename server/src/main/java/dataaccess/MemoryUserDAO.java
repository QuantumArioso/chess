package dataaccess;

public class MemoryUserDAO {
    private String username;
    private String password;
    private String email;

    public MemoryUserDAO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
