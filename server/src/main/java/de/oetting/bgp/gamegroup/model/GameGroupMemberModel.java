package de.oetting.bgp.gamegroup.model;

import de.oetting.bgp.gamegroup.persistence.GameGroupMemberType;

public class GameGroupMemberModel {

    private long id;
    private String name;
    private GameGroupMemberType type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameGroupMemberType getType() {
        return type;
    }

    public void setType(GameGroupMemberType type) {
        this.type = type;
    }
}
