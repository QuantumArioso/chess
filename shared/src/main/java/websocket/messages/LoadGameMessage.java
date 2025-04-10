package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    int game;

    public LoadGameMessage(int game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
}
