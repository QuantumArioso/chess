package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlGameDAO extends SqlDAO implements GameDAO {
    public GameData addNewGame(String gameName) {
        throw new RuntimeException("Not implemented");
    }

    public GameData getGameData(int gameID) {
        throw new RuntimeException("Not implemented");
    }

    public GameData updateGamePlayer(int gameID, ChessGame.TeamColor playerColor, String username) {
        throw new RuntimeException("Not implemented");
    }

    public ArrayList<GameData> getAllGameData() {
        throw new RuntimeException("Not implemented");
    }

    public void deleteAllGameData() throws DataAccessException, SQLException {
        Connection conn = DatabaseManager.getConnection();
        var statement = "TRUNCATE TABLE game";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        }
    }
}
