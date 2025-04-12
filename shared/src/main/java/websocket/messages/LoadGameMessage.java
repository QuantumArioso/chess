package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    ChessGame game;
    boolean needToFlip;

    public LoadGameMessage(ChessGame game, boolean needToFlip) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.needToFlip = needToFlip;
    }

    public boolean isNeedToFlip() {
        return needToFlip;
    }

    public ChessGame getGame() {
        return game;
    }
}
