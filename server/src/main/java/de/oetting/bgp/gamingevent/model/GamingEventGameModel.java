package de.oetting.bgp.gamingevent.model;

import de.oetting.bgp.game.model.GameModel;
import de.oetting.bgp.gamingevent.GameEventStatus;

public class GamingEventGameModel {

    private GameModel game;
    private GameEventStatus gameStatus;
    private String comment;

    public GameModel getGame() {
        return game;
    }

    public void setGame(GameModel game) {
        this.game = game;
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
