package de.oetting.wwp.service.events;

public class GameGroupEvent implements Event {

    private final String source;
    private final long gameGroupId;
    private final GameGroupEventType eventType;
    private final String description;

    public GameGroupEvent(String source, long gameGroupId, GameGroupEventType eventType, String description) {
        this.source = source;
        this.gameGroupId = gameGroupId;
        this.eventType = eventType;
        this.description = description;
    }

    public String getSource() {
        return source;
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
