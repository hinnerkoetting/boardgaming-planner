package de.oetting.wwp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Entity
public class Player {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    @Length(min = 3, max = 35)
    private String name;
    @ManyToMany(mappedBy = "players")
    @JsonBackReference
    private Set<GameGroup> gameGroups;

    public Long getId() {
        return id;
    }

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

    void addGameGroup(GameGroup gameGroup) {
        this.gameGroups.add(gameGroup);
    }

    public void deleteGameGroup(GameGroup gameGroup) {
        this.gameGroups.remove(gameGroup);
    }


}
