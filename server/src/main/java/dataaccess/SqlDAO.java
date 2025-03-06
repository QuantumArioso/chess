package dataaccess;

public class SqlDAO {
    static {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
