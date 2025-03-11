package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SqlUserDAO extends SqlDAO implements UserDAO {
    private final Properties properties = new Properties();

    @Override
    public UserData getUser(String username) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void createUser(UserData userData) throws SQLException {
        String username = userData.username();
        String hashedPassword = hashUserPassword(userData.password());
        String email = userData.email();
        var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pet_store",
                properties.getProperty("db.user"), properties.getProperty("db.password"));

        if (username.matches("[a-zA-Z]+")) {
            var statement = "INSERT INTO users (username, hashedPassword, email) VALUES(?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, email);
                preparedStatement.executeUpdate();
            }
        }
    }

    private String hashUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    @Override
    public void deleteAllUserData() {
        throw new RuntimeException("Not implemented");
    }

//    private boolean verifyUser(String username, String providedClearTextPassword) {
//        // read the previously hashed password from the database
//        var hashedPassword = readHashedPasswordFromDatabase(username);
//
//        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
//    }
}
