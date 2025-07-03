package de.oetting.wwp.service.events;

public class GameGroupEvent implements Event {

    private final String source;
    private long gameGroupId;
    private final GameGroupEventType eventType;

    public GameGroupEvent(String source, long gameGroupId, GameGroupEventType eventType) {
        this.source = source;
        this.gameGroupId = gameGroupId;
        this.eventType = eventType;
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
}
