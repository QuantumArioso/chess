package service;

import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import handler.*;

public class GameService {
    public GameListResult listGames(GameListRequest gameListRequest) {throw new RuntimeException("Not implemented");}

    public GameCreateResult createGame(GameCreateRequest gameCreateRequest) {throw new RuntimeException("Not implemented");}

    public void joinGame(GameJoinRequest gameJoinRequest) {throw new RuntimeException("Not implemented");}

//    public void clearGameData() {
//        GameDAO gameDAO = new MemoryGameDAO();
//        gameDAO.deleteUserData();
//    }
}
