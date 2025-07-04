package de.oetting.bgp.service.events;

public interface EventListener {
    boolean shouldSend(Event event);

    void send(Event event);

}
