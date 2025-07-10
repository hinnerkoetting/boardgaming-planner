package de.oetting.bgp.service.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseEmitterService {

    private static final Logger LOG = LoggerFactory.getLogger(SseEmitterService.class);
    private final Map<UUID, EventListener> listeners = new ConcurrentHashMap<>();

    @Transactional(propagation = Propagation.NEVER) // will leak a connection if transaction is open
    public SseEmitter createGameGroupEmitter(int gameGroupId) {
        LOG.debug("Subscribing to events on gameGroup {}", gameGroupId);
        UUID uuid = UUID.randomUUID();
        SseEmitter emitter = createEmitter(uuid);
        var listener = new GameGroupEventListener(gameGroupId, emitter);

        listeners.put(uuid, listener);
        return emitter;
    }

    private SseEmitter createEmitter(UUID uuid) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> removeEmitter(uuid));
        emitter.onTimeout(() -> removeEmitter(uuid));
        emitter.onError((__) -> removeEmitter(uuid));

        return emitter;
    }

    private void removeEmitter(UUID uuid) {
        listeners.remove(uuid);
    }

    public void onEventInGameGroup(GameGroupEvent gameGroupEvent) {
        listeners.values().stream()
                .filter(l -> l.shouldSend(gameGroupEvent))
                .forEach(l -> l.send(gameGroupEvent));
    }

}
