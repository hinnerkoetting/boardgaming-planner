package de.oetting.wwp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Player {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "players")
    @JsonBackReference
    private Set<GameGroup> gameGroups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GameGroup> getGameGroups() {
        return gameGroups;
    }

    public void setGameGroups(Set<GameGroup> gameGroups) {
        this.gameGroups = gameGroups;
    }

    public void addGameGroup(GameGroup gameGroup) {
        this.gameGroups.add(gameGroup);
    }
}
