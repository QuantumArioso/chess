package dataaccess;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {
    GameData addNewGame(String gameName);

    GameData getGameData(int gameID);

    GameData updateGamePlayer(int gameID, ChessGame.TeamColor playerColor, String username);

    void deleteAllGameData();
}
