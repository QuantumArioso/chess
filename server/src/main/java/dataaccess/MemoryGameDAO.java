package dataaccess;

import chess.ChessGame;
import db.MockedDB;
import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    public static int gameCounter = 0;

    @Override
    public GameData addNewGame(String gameName) {
        GameData gameData = new GameData(gameCounter, null, null, gameName, new ChessGame());
        gameCounter += 1;
        MockedDB.allGameData.add(gameData);
        return gameData;
    }

    @Override
    public GameData getGameData(int gameID) throws BadRequestException {
        for (GameData data : MockedDB.allGameData) {
            if (gameID == data.gameID()) {
                return data;
            }
        }
        throw new BadRequestException();
    }

    @Override
    public GameData updateGamePlayer(int gameID, ChessGame.TeamColor playerColor, String username) {
        GameData gameData = null;
        for (GameData data : MockedDB.allGameData) {
            if (gameID == data.gameID()) {
                MockedDB.allGameData.remove(data);
                gameData = data;
                break;
            }
        }
        if (gameData == null) {
            throw new BadRequestException();
        }

        if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
            return new GameData(gameID, username, gameData.blackUsername(), gameData.gameName(), gameData.game());
        }
        else {
            return new GameData(gameID, gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
        }
    }

    @Override
    public void deleteAllGameData() {
        MockedDB.allGameData = new ArrayList<>();
        gameCounter = 0;
    }
}
