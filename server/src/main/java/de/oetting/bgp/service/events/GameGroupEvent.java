package de.oetting.bgp.service.events;

public class GameGroupEvent implements Event {

    private final long sourcePlayerId;
    private final String sourcePlayerName;
    private final long gameGroupId;
    private final GameGroupEventType eventType;
    private final String description;

    public GameGroupEvent(long sourcePlayerId, String sourcePlayerName, long gameGroupId, GameGroupEventType eventType, String description) {
        this.sourcePlayerId = sourcePlayerId;
        this.sourcePlayerName = sourcePlayerName;
        this.gameGroupId = gameGroupId;
        this.eventType = eventType;
        this.description = description;
    }

    public long getSourcePlayerId() {
        return sourcePlayerId;
    }

    public String getSourcePlayerName() {
        return sourcePlayerName;
    }

    public long getGameGroupId() {
        return gameGroupId;
    }

    public GameGroupEventType getEventType() {
        return eventType;
    }

    public String getDescription() {
        return description;
    }
}
