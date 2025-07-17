package de.oetting.bgp.gamingevent.model;

import de.oetting.bgp.entities.Player;
import de.oetting.bgp.gamingevent.ParticipationStatus;

public class GamingEventParticipantsModel {


    private Player participant;
    private ParticipationStatus participationStatus;
    private String comment;


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
