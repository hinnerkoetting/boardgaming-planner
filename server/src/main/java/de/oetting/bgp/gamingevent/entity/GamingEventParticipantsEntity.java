package de.oetting.bgp.gamingevent.entity;

import de.oetting.bgp.entities.Player;
import de.oetting.bgp.gamingevent.ParticipationStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "PLAYER_2_GAMING_EVENT")
public class GamingEventParticipantsEntity {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="GAMING_EVENT_ID", nullable=false, updatable=false)
    private GamingEventEntity gamingEvent;

    @ManyToOne
    @JoinColumn(name="PLAYER_ID", nullable=false, updatable=false)
    private Player participant;

    @Enumerated(EnumType.STRING)
    @Column(name = "PARTICIPATION_STATUS")
    private ParticipationStatus participationStatus;

    @Column(name = "COMMENT")
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GamingEventEntity getGamingEvent() {
        return gamingEvent;
    }

    public void setGamingEvent(GamingEventEntity gamingEvent) {
        this.gamingEvent = gamingEvent;
    }

    public Player getParticipant() {
        return participant;
    }

    public void setParticipant(Player participant) {
        this.participant = participant;
    }

    public ParticipationStatus getParticipationStatus() {
        return participationStatus;
    }

    public void setParticipationStatus(ParticipationStatus participationStatus) {
        this.participationStatus = participationStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
