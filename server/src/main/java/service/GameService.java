package service;

import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import handler.*;

public class GameService {
    public GameListResult listGames(GameListRequest gameListRequest) {throw new RuntimeException("Not implemented");}

    public GameCreateResult createGame(GameCreateRequest gameCreateRequest) {throw new RuntimeException("Not implemented");}

    public void joinGame(GameJoinRequest gameJoinRequest) {throw new RuntimeException("Not implemented");}

    public static void clearGameData() {
        GameDAO gameDAO = new MemoryGameDAO();
        gameDAO.deleteAllGameData();
    }
}
