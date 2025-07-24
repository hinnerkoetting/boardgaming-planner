package de.oetting.bgp.gamingevent.model;

import de.oetting.bgp.gamingevent.ParticipationStatus;
import de.oetting.bgp.player.model.PublicPlayerModel;

public class GamingEventParticipantsModel {

    private PublicPlayerModel participant;
    private ParticipationStatus participationStatus;
    private String comment;

    public PublicPlayerModel getParticipant() {
        return participant;
    }

    public void setParticipant(PublicPlayerModel participant) {
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
