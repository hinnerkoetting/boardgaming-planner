package de.oetting.bgp.gamegroup.persistence;

import java.io.Serializable;

public class GameGroup2PlayerId implements Serializable {

    private Long playerId;
    private Long gameGroupId;

    public GameGroup2PlayerId() {
    }

    public GameGroup2PlayerId(Long playerId, Long gameGroupId) {
        this.playerId = playerId;
        this.gameGroupId = gameGroupId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getGameGroupId() {
        return gameGroupId;
    }

    public void setGameGroupId(Long gameGroupId) {
        this.gameGroupId = gameGroupId;
    }
}