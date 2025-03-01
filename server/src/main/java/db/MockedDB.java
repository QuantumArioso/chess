package db;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MockedDB {
    public static Collection<UserData> allUserData = new ArrayList<>();
    public static Collection<AuthData> allAuthData = new ArrayList<>();
    public static Collection<GameData> allGameData = new ArrayList<>();
}
