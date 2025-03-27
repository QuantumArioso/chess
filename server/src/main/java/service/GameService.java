package service;

import chess.ChessGame;
import dataaccess.*;
import db.MockedDB;
import handler.*;
import model.GameData;

import java.sql.SQLException;

public class GameService {
    public GameListResult listGames(GameListRequest gameListRequest) throws UnauthorizedException {
        AuthService.validateAuth(gameListRequest.authToken());
        GameDAO gameDAO = new SqlGameDAO();

        return new GameListResult(SqlGameDAO.getAllGameData());
    }

    public GameCreateResult createGame(GameCreateRequest gameCreateRequest) throws DataAccessException {
        AuthService.validateAuth(gameCreateRequest.authToken());

        GameDAO gameDAO = new SqlGameDAO();
        GameData gameData = gameDAO.addNewGame(gameCreateRequest.gameName());
        if (gameData == null) {
            throw new BadRequestException();
        }

        return new GameCreateResult(gameData.gameID());
    }

    public EmptyResult joinGame(GameJoinRequest gameJoinRequest) throws DataAccessException {
        String authToken = gameJoinRequest.authToken();
        AuthService.validateAuth(authToken);
        AuthDAO authDAO = new SqlAuthDAO();
        String username = authDAO.getAuth(authToken).username();
        int gameID = gameJoinRequest.gameID();

        GameDAO gameDAO = new SqlGameDAO();
        GameData gameData = gameDAO.getGameData(gameID);

        if (!playerNotInUse(gameData, gameJoinRequest.playerColor())) {
            throw new UnavailableException();
        }

        MockedDB.allGameData.add(gameDAO.updateGamePlayer(gameID, gameJoinRequest.playerColor(), username));

        return new EmptyResult();
    }

    private static boolean playerNotInUse(GameData gameData, ChessGame.TeamColor playerColor) {
        if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
            return gameData.whiteUsername() == null;
        }
        return gameData.blackUsername() == null;
    }

    public static void clearGameData() throws SQLException, DataAccessException {
        GameDAO gameDAO = new SqlGameDAO();
        gameDAO.deleteAllGameData();
    }
}
