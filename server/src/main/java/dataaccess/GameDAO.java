package dataaccess;

import model.GameData;

public interface GameDAO {
    GameData addNewGame(String gameName);

    void deleteAllGameData();
}
