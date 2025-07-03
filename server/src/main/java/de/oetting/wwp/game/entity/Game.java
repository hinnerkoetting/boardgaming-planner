package de.oetting.wwp.game.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.tags.entity.TagEntity;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Entity
public class Game {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @Length(min = 2, max = 255)
    private String name;

    @Column(length = 4096)
    private String description;

    private String imageUrl;
    private String thumbnailUrl;

    private Integer minPlayers;
    private Integer maxPlayers;
    private Integer playingTimeMinutes;
    @Column(length = 1000)
    private String url;

    @ManyToMany(mappedBy = "games")
    @JsonBackReference
    private Set<GameGroup> gameGroups;

    @ManyToMany
    @JoinTable(name="GAMES_2_TAGS",
            joinColumns=
            @JoinColumn(name="GAME_ID", referencedColumnName="ID"),
            inverseJoinColumns=
            @JoinColumn(name="TAG_ID", referencedColumnName="ID"))
    private Set<TagEntity> globalTags;

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

    public void addGameGroup(GameGroup gameGroup) {
        this.gameGroups.add(gameGroup);
    }

    public void deleteGameGroup(GameGroup gameGroup) {
        this.gameGroups.remove(gameGroup);
    }

    public Set<TagEntity> getGlobalTags() {
        return globalTags;
    }

    public void setGlobalTags(Set<TagEntity> globalTags) {
        this.globalTags = globalTags;
    }

    public void addGlobalTag(TagEntity tag) {
        globalTags.add(tag);
    }

    public Integer getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(Integer minPlayers) {
        this.minPlayers = minPlayers;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Integer getPlayingTimeMinutes() {
        return playingTimeMinutes;
    }

    public void setPlayingTimeMinutes(Integer playingTimeMinutes) {
        this.playingTimeMinutes = playingTimeMinutes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
