package dataaccess;

import chess.ChessGame;
import db.MockedDB;
import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    private static int gameCounter = 0;

    public GameData addNewGame(String gameName) {
        GameData gameData = new GameData(gameCounter, null, null, gameName, new ChessGame());
        gameCounter += 1;
        return gameData;
    }

    public void deleteAllGameData() {
        MockedDB.allGameData = new ArrayList<>();
        gameCounter = 0;
    }
}
