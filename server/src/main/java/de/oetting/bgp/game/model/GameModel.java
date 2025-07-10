package de.oetting.bgp.game.model;

import java.util.List;

public class GameModel {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String thumbnailUrl;
    private Integer minPlayers;
    private Integer maxPlayers;
    private Integer playingTimeMinutes;
    private String url;
    private List<Integer> recommendedNumberOfPlayers;
    private List<Integer> bestNumberOfPlayers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Integer> getRecommendedNumberOfPlayers() {
        return recommendedNumberOfPlayers;
    }

    public void setRecommendedNumberOfPlayers(List<Integer> recommendedNumberOfPlayers) {
        this.recommendedNumberOfPlayers = recommendedNumberOfPlayers;
    }

    public List<Integer> getBestNumberOfPlayers() {
        return bestNumberOfPlayers;
    }

    public void setBestNumberOfPlayers(List<Integer> bestNumberOfPlayers) {
        this.bestNumberOfPlayers = bestNumberOfPlayers;
    }
}
