package de.oetting.bgp.gamegroup.persistence;

import java.io.Serializable;

public class Game2GameGroupId implements Serializable {

    private Long gameId;
    private Long gameGroupId;

    public Game2GameGroupId() {
    }

    public Game2GameGroupId(Long gameId, Long gameGroupId) {
        this.gameId = gameId;
        this.gameGroupId = gameGroupId;
    }

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
