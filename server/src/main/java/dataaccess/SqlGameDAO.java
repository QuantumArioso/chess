package dataaccess;

import chess.ChessGame;
import model.GameData;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlGameDAO extends SqlDAO implements GameDAO {
    public static int gameCounter = getAllGameData().size();

    public GameData addNewGame(String gameName) {
        if (gameName.isEmpty()) {
            return null;
        }
        int size = getAllGameData().size();

        ChessGame chessGame = new ChessGame();
        String jsonChessGame = convertGameToJson(chessGame);
        try (Connection conn = DatabaseManager.getConnection()){
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
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertGameToJson(ChessGame chessGame) {
        var serializer = new Gson();
        return serializer.toJson(chessGame);
    }

    public GameData getGameData(int gameID) {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=? ";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                try (var results = preparedStatement.executeQuery()) {
                    while (results.next()) {
                        String dbWhiteUsername = getUsername(results.getString("whiteUsername"));
                        String dbBlackUsername = getUsername(results.getString("blackUsername"));
                        String dbGameName = results.getString("gameName");
                        String dbJsonGame = results.getString("game");
                        ChessGame dbGame = convertJsonToGame(dbJsonGame);
                        return new GameData(gameID, dbWhiteUsername, dbBlackUsername, dbGameName, dbGame);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getUsername(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        return username;
    }

    private static ChessGame convertJsonToGame(String jsonGame) {
        var serializer = new Gson();
        return serializer.fromJson(jsonGame, ChessGame.class);
    }

    public GameData updateGamePlayer(int gameID, ChessGame.TeamColor playerColor, String username) {
        try (Connection conn = DatabaseManager.getConnection()){
            GameData oldGameData = getGameData(gameID);
            if (oldGameData == null) {
                return null;
            }
            String oldWhiteUsername = oldGameData.whiteUsername();
            if (oldWhiteUsername == null) {
                oldWhiteUsername = "";
            }
            String oldBlackUsername = oldGameData.blackUsername();
            if (oldBlackUsername == null) {
                oldBlackUsername = "";
            }

            var statement = "UPDATE game SET whiteUsername = ?, blackUsername = ? WHERE gameID = ?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(3, gameID);
                if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, oldBlackUsername);
                }
                else {
                    preparedStatement.setString(1, oldWhiteUsername);
                    preparedStatement.setString(2, username);
                }
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return getGameData(gameID);
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GameData updateChessGame(String game, int gameID) {
        try (Connection conn = DatabaseManager.getConnection()){
            GameData oldGameData = getGameData(gameID);
            if (oldGameData == null) {
                return null;
            }

            var statement = "UPDATE game SET game = ? WHERE gameID = ?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, game);
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return getGameData(gameID);
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<GameData> getAllGameData() {
        try (Connection conn = DatabaseManager.getConnection()) {
            ArrayList<GameData> allGameData = new ArrayList<>();
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game ";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (var results = preparedStatement.executeQuery()) {
                    while (results.next()) {
                        int dbGameID = results.getInt("gameID");
                        String dbWhiteUsername = getUsername(results.getString("whiteUsername"));
                        String dbBlackUsername = getUsername(results.getString("blackUsername"));
                        String dbGameName = results.getString("gameName");
                        String dbJsonGame = results.getString("game");
                        ChessGame dbGame = convertJsonToGame(dbJsonGame);
                        allGameData.add(new GameData(dbGameID, dbWhiteUsername, dbBlackUsername, dbGameName, dbGame));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return allGameData;
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAllGameData() throws DataAccessException, SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE TABLE game";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
            gameCounter = 0;
        }
    }
}
