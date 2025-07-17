package de.oetting.bgp.service.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public class GameGroupEventListener implements EventListener {

    private static final Logger LOG = LoggerFactory.getLogger(SseEmitterService.class);
    private final long listeningGameGroup;
    private final SseEmitter sseEmitter;

    public GameGroupEventListener(int listeningGameGroup, SseEmitter sseEmitter) {
        this.listeningGameGroup = listeningGameGroup;
        this.sseEmitter = sseEmitter;
    }

    @Override
    public boolean shouldSend(Event event) {
        if (!(event instanceof GameGroupEvent)) {
            return false;
        }
        return listeningGameGroup == ((GameGroupEvent) event).getGameGroupId();
    }

    @Override
    public void send(Event event) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("GROUP_CHANGED")
                    .data(event));
        } catch (IOException e) {
            LOG.debug("SSE-Event Verbindung abgebrochen {}", e.getMessage());
        }
    }

}
