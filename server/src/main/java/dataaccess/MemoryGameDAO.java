package dataaccess;

import db.MockedDB;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    @Override
    public void deleteAllGameData() {
        MockedDB.allGameData = new ArrayList<>();
    }
}
