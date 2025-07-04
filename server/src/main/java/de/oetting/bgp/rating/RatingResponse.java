package de.oetting.bgp.rating;

public class RatingResponse {
    private long playerId;
    private long gameGroupId;
    private long gameId;
    private int rating;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getGameGroupId() {
        return gameGroupId;
    }

    public void setGameGroupId(long gameGroupId) {
        this.gameGroupId = gameGroupId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
