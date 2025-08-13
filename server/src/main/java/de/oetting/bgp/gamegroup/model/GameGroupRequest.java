package de.oetting.bgp.gamegroup.model;

import de.oetting.bgp.gamegroup.persistence.GameGroupType;

public class GameGroupRequest {

    private String name;
    private GameGroupType type;
    private boolean openForNewPlayers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameGroupType getType() {
        return type;
    }

    public void setType(GameGroupType type) {
        this.type = type;
    }

    public boolean isOpenForNewPlayers() {
        return openForNewPlayers;
    }

    public void setOpenForNewPlayers(boolean openForNewPlayers) {
        this.openForNewPlayers = openForNewPlayers;
    }
}
