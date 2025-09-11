package de.oetting.bgp.gamingevent.entity;

import de.oetting.bgp.gamegroup.persistence.GameGroupEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "GAMING_EVENT")
public class GamingEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = "START_TS")
    private ZonedDateTime start;

    @ManyToOne
    @JoinColumn(name = "GAME_GROUP_ID", nullable = false, updatable = false)
    private GameGroupEntity gameGroup;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private Schedule schedule;

    @ManyToMany(mappedBy = "gamingEvent")
    private List<GamingEventParticipantsEntity> participants;

    @ManyToMany(mappedBy = "gamingEvent")
    private List<GamingEventGameEntity> games;

    // For non-recurring events the parent event can be the reference to the recurring event it belongs to.
    @OneToOne
    @JoinColumn(name = "PARENT_EVENT_ID")
    private GamingEventEntity parent;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GameGroupEntity getGameGroup() {
        return gameGroup;
    }

    public void setGameGroup(GameGroupEntity gameGroup) {
        this.gameGroup = gameGroup;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
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

    public GamingEventEntity getParent() {
        return parent;
    }

    public void setParent(GamingEventEntity parent) {
        this.parent = parent;
    }
}
