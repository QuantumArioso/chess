package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import handler.*;
import model.GameData;

public class GameService {
    public GameListResult listGames(GameListRequest gameListRequest) {throw new RuntimeException("Not implemented");}

    public GameCreateResult createGame(GameCreateRequest gameCreateRequest) throws DataAccessException {
        AuthService.validateAuth(gameCreateRequest.authToken());

        GameDAO gameDAO = new MemoryGameDAO();
        GameData gameData = gameDAO.addNewGame(gameCreateRequest.gameName());

        return new GameCreateResult(gameData.gameID());
    }

    public void joinGame(GameJoinRequest gameJoinRequest) throws DataAccessException {
        AuthService.validateAuth(gameJoinRequest.authToken());


    }

    public static void clearGameData() {
        GameDAO gameDAO = new MemoryGameDAO();
        gameDAO.deleteAllGameData();
    }
}
