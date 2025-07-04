package de.oetting.bgp.gamingevent.model;

import de.oetting.bgp.game.entity.Game;
import de.oetting.bgp.gamingevent.GameEventStatus;
import de.oetting.bgp.gamingevent.entity.GamingEventEntity;
import jakarta.persistence.*;

public class GamingEventGameModel {

    private Game game;
    private GameEventStatus gameStatus;
    private String comment;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
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
