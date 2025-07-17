package de.oetting.bgp.gamingevent.model;

import de.oetting.bgp.gamingevent.GameEventStatus;

public class AddGameToEventRequest {

    private long gameId;
    private GameEventStatus gameStatus;
    private String comment;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public GameEventStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameEventStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
