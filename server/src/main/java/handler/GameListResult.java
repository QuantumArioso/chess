package handler;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public record GameListResult(ArrayList<GameData> games) {
}
