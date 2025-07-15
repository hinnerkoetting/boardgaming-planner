package de.oetting.bgp.game.model;

import java.util.List;
import java.util.Map;

public class GameStatisticsModel {

    private Long lastPlayed;
    private long playedNumberOfTimes;
    private Map<String, Long> numberOfTimesPlayedByPlayers;
    private List<Long> playDates;

    public Long getLastPlayed() {
        return lastPlayed;
    }

    public long getPlayedNumberOfTimes() {
        return playedNumberOfTimes;
    }

    public Map<String, Long> getNumberOfTimesPlayedByPlayers() {
        return numberOfTimesPlayedByPlayers;
    }

    public void setLastPlayed(Long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public void setPlayedNumberOfTimes(long playedNumberOfTimes) {
        this.playedNumberOfTimes = playedNumberOfTimes;
    }

    public void setNumberOfTimesPlayedByPlayers(Map<String, Long> numberOfTimesPlayedByPlayers) {
        this.numberOfTimesPlayedByPlayers = numberOfTimesPlayedByPlayers;
    }

    public List<Long> getPlayDates() {
        return playDates;
    }

    public void setPlayDates(List<Long> playDates) {
        this.playDates = playDates;
    }
}
