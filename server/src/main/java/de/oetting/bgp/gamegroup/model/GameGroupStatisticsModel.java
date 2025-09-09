package de.oetting.bgp.gamegroup.model;

import de.oetting.bgp.gamingevent.model.GamingEventModel;

import java.util.List;

public class GameGroupStatisticsModel {

    private List<GamingEventModel> events;

    public List<GamingEventModel> getEvents() {
        return events;
    }

    public void setEvents(List<GamingEventModel> events) {
        this.events = events;
    }
}
