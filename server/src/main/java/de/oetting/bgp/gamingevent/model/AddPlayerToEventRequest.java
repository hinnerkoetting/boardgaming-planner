package de.oetting.bgp.gamingevent.model;

import de.oetting.bgp.gamingevent.ParticipationStatus;

public class AddPlayerToEventRequest {

    private long playerId;
    private ParticipationStatus participationStatus;
    private String comment;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
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
