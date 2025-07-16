package de.oetting.bgp.game.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import de.oetting.bgp.gamegroup.persistence.Game2GameGroupRelation;
import de.oetting.bgp.gamegroup.persistence.GameGroup;
import de.oetting.bgp.tags.entity.TagEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

import java.util.*;
import java.util.stream.Collectors;

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

    @Min(0)
    @Max(9999)
    private Integer minPlayers;

    @Max(9999)
    private Integer maxPlayers;

    @Min(0)
    private Integer playingTimeMinutes;

    @Column(length = 1000)
    private String url;

    // Encoded as comma-separed list to avoid additional tables, e.g.
    // 1,4,5
    @Column
    private String recommendedNumberOfPlayers;

    // Encoded as comma-separed list to avoid additional tables, e.g.
    // 1,4,5
    @Column
    private String bestNumberOfPlayers;

    @OneToMany(mappedBy = "game")
    @JsonBackReference
    private Set<Game2GameGroupRelation> gameGroups;

    @ManyToMany
    @JoinTable(name="GAMES_2_TAGS",
            joinColumns=
            @JoinColumn(name="GAME_ID", referencedColumnName="ID"),
            inverseJoinColumns=
            @JoinColumn(name="TAG_ID", referencedColumnName="ID"))
    private Set<TagEntity> globalTags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Game2GameGroupRelation> getGameGroups() {
        return gameGroups;
    }

    public void setGameGroups(Set<Game2GameGroupRelation> gameGroups) {
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

    public void addGameGroup(Game2GameGroupRelation gameGroup) {
        this.gameGroups.add(gameGroup);
    }

    public void deleteGameGroup(GameGroup gameGroup) {
        this.gameGroups.removeIf(g -> Objects.equals(g.getGameGroup().getId(), gameGroup.getId()));
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

    public Set<Integer> getRecommendedNumberOfPlayers() {
        return parseNumberOfPlayers(recommendedNumberOfPlayers);
    }

    public void setRecommendedNumberOfPlayers(Set<Integer> recommendedNumberOfPlayers) {
        this.recommendedNumberOfPlayers = encodeNumberOfPlayers(recommendedNumberOfPlayers);
    }

    public Set<Integer> getBestNumberOfPlayers() {
        return parseNumberOfPlayers(bestNumberOfPlayers);
    }

    public void setBestNumberOfPlayers(Set<Integer> bestNumberOfPlayers) {
        this.bestNumberOfPlayers = encodeNumberOfPlayers(bestNumberOfPlayers);
    }

    private Set<Integer> parseNumberOfPlayers(String value) {
        if (value == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(value.split(","))
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    private String encodeNumberOfPlayers(Set<Integer> bestNumberOfPlayers) {
        return bestNumberOfPlayers.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }
}
