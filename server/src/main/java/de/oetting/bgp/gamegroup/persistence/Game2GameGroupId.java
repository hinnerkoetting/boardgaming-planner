package de.oetting.bgp.gamegroup.persistence;

import java.io.Serializable;

public class Game2GameGroupId implements Serializable {

    private Long gameId;
    private Long gameGroupId;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGameGroupId() {
        return gameGroupId;
    }

    public void setGameGroupId(Long gameGroupId) {
        this.gameGroupId = gameGroupId;
    }
}
