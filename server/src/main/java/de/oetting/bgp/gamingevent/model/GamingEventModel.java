package de.oetting.bgp.gamingevent.model;

import java.time.ZonedDateTime;
import java.util.List;

public class GamingEventModel {

    private Long id;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private long gameGroupId;
    private String description;
    private List<GamingEventGameModel> games;
    private List<GamingEventParticipantsModel> participants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getGameGroupId() {
        return gameGroupId;
    }

    public void setGameGroupId(long gameGroupId) {
        this.gameGroupId = gameGroupId;
    }

    public List<GamingEventGameModel> getGames() {
        return games;
    }

    public void setGames(List<GamingEventGameModel> games) {
        this.games = games;
    }

    public List<GamingEventParticipantsModel> getParticipants() {
        return participants;
    }

    public void setParticipants(List<GamingEventParticipantsModel> participants) {
        this.participants = participants;
    }
}
