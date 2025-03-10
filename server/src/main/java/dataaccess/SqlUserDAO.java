package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlUserDAO extends SqlDAO implements UserDAO {
    @Override
    public UserData getUser(String username) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void createUser(UserData userData) throws SQLException {
        String username = userData.username();
        var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pet_store", "root", "monkeypie");

        if (username.matches("[a-zA-Z]+")) {
            var statement = "INSERT INTO pet (name) VALUES(?)";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public void deleteAllUserData() {
        throw new RuntimeException("Not implemented");
    }

    private String hashUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    private boolean verifyUser(String username, String providedClearTextPassword) {
        // read the previously hashed password from the database
        var hashedPassword = readHashedPasswordFromDatabase(username);

        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }
}
