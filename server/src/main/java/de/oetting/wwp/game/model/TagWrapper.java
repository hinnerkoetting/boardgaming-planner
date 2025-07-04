package de.oetting.wwp.game.model;

import java.util.List;

public class TagWrapper {

    private List<TagModel> global;
    private List<TagModel> group;
    private List<TagModel> player;

    public List<TagModel> getGlobal() {
        return global;
    }

    public void setGlobal(List<TagModel> global) {
        this.global = global;
    }

    public List<TagModel> getGroup() {
        return group;
    }

    public void setGroup(List<TagModel> group) {
        this.group = group;
    }

    public List<TagModel> getPlayer() {
        return player;
    }

    public void setPlayer(List<TagModel> player) {
        this.player = player;
    }
}
