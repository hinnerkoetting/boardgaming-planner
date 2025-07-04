package de.oetting.bgp.gamingevent.model;

import java.time.ZonedDateTime;

public class GamingEventModel {

    private Long id;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private long gameGroupId;
    private String description;

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
}
