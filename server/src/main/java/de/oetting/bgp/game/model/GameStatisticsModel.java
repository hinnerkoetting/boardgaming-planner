package de.oetting.bgp.game.model;

import java.util.Map;

public class GameStatisticsModel {

    private Long lastPlayed;
    private long playedNumberOfTimes;
    private Map<Long, Integer> numberOfTimesPlayedByPlayers;

    public Long getLastPlayed() {
        return lastPlayed;
    }

    public long getPlayedNumberOfTimes() {
        return playedNumberOfTimes;
    }

    public Map<Long, Integer> getNumberOfTimesPlayedByPlayers() {
        return numberOfTimesPlayedByPlayers;
    }

    public void setLastPlayed(Long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public void setPlayedNumberOfTimes(long playedNumberOfTimes) {
        this.playedNumberOfTimes = playedNumberOfTimes;
    }

    public void setNumberOfTimesPlayedByPlayers(Map<Long, Integer> numberOfTimesPlayedByPlayers) {
        this.numberOfTimesPlayedByPlayers = numberOfTimesPlayedByPlayers;
    }
}
