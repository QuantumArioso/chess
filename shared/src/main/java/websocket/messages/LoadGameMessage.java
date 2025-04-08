package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    int game;

    public LoadGameMessage(ServerMessageType type, int game) {
        super(type);
        this.game = game;
    }
}
