package de.oetting.wwp.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Game {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;

    @Column(length = 4096)
    private String description;

    private String imageUrl;
    private String thumbnailUrl;

    @ManyToMany(mappedBy = "games")
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    void addGameGroup(GameGroup gameGroup) {
        this.gameGroups.add(gameGroup);
    }

    void deleteGameGroup(GameGroup gameGroup) {
        this.gameGroups.remove(gameGroup);
    }
}
