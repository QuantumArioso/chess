package dataaccess;

import chess.ChessGame;
import model.GameData;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class SqlGameDAO extends SqlDAO implements GameDAO {
    public static int gameCounter = 0;

    public GameData addNewGame(String gameName) {
        ChessGame chessGame = new ChessGame();
        String jsonChessGame = convertToJSON(chessGame);
        Connection conn;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        if (gameName.matches("[a-zA-Z _]+")) {
            var statement = "INSERT INTO game (gameName, game) VALUES(?, ?) ";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, gameName);
                preparedStatement.setString(2, jsonChessGame);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            gameCounter++;
            return new GameData(gameCounter, null, null, gameName, chessGame);
        }

        //how do I get the gameID? two different games could have the same name...
        return null;
    }

    private String convertToJSON(ChessGame chessGame) {
        var serializer = new Gson();
        return serializer.toJson(chessGame);
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
        gameCounter = 0;
    }
}
