package de.oetting.bgp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.oetting.bgp.gamegroup.persistence.GameGroupEntity;
import de.oetting.bgp.gamegroup.persistence.GameGroupMembership;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @Length(min = 3, max = 35)
    private String name;

    @OneToMany(mappedBy = "player")
    @JsonBackReference
    private Set<GameGroupMembership> gameGroups;

    @ManyToOne
    @JoinColumn(name = "GAME_GROUP_ID")
    private GameGroupEntity personalCollection;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GameGroupMembership> getGameGroups() {
        return gameGroups;
    }

    public void setGameGroups(Set<GameGroupMembership> gameGroups) {
        this.gameGroups = gameGroups;
    }

    public void addGameGroup(GameGroupMembership gameGroup) {
        this.gameGroups.add(gameGroup);
    }

    public void deleteGameGroup(GameGroupEntity gameGroup) {
        this.gameGroups.remove(gameGroup);
    }

    public GameGroupEntity getPersonalCollection() {
        return personalCollection;
    }

    public void setPersonalCollection(GameGroupEntity personalCollection) {
        this.personalCollection = personalCollection;
    }

}
