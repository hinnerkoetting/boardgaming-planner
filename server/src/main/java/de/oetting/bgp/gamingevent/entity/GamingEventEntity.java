package de.oetting.bgp.gamingevent.entity;

import de.oetting.bgp.gamegroup.entity.GameGroup;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "GAMING_EVENT")
public class GamingEventEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = "START_TS")
    private ZonedDateTime start;

    @Column(nullable = false, name = "END_TS")
    private ZonedDateTime end;

    @ManyToOne
    @JoinColumn(name="GAME_GROUP_ID", nullable=false, updatable=false)
    private GameGroup gameGroup;

    @Column
    private String description;

    @ManyToMany(mappedBy = "gamingEvent")
    private List<GamingEventParticipantsEntity> participants;

    @ManyToMany(mappedBy = "gamingEvent")
    private List<GamingEventGameEntity> games;

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

    public GameGroup getGameGroup() {
        return gameGroup;
    }

    public void setGameGroup(GameGroup gameGroup) {
        this.gameGroup = gameGroup;
    }

    public List<GamingEventParticipantsEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<GamingEventParticipantsEntity> participants) {
        this.participants = participants;
    }


    public List<GamingEventGameEntity> getGames() {
        return games;
    }

    public void setGames(List<GamingEventGameEntity> games) {
        this.games = games;
    }
}
