package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public interface GameDAO {
    GameData addNewGame(String gameName);

    GameData getGameData(int gameID);

    GameData updateGamePlayer(int gameID, ChessGame.TeamColor playerColor, String username);

    ArrayList<GameData> getAllGameData();

    void deleteAllGameData() throws DataAccessException, SQLException;
}
