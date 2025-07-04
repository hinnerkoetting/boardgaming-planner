package de.oetting.bgp.gamingevent.model;

import de.oetting.bgp.gamingevent.entity.Schedule;

import java.util.List;

public class  GamingEventModel {

    private Long id;
    private long start;
    private long gameGroupId;
    private String description;
    private List<GamingEventGameModel> games;
    private List<GamingEventParticipantsModel> participants;
    private Schedule schedule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
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

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
