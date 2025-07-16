package de.oetting.bgp.game.model;

import de.oetting.bgp.rating.RatingModel;

import java.util.Set;

public class RatedGameModel {

    private Long id;
    private String name;
    private String description;

    private String thumbnailUrl;
    private String imageUrl;
    private RatingModel rating;
    private Integer minPlayers;
    private Integer maxPlayers;
    private Integer playingTimeMinutes;
    private String url;

    private TagWrapper tags;
    private Set<Integer> recommendedNumberOfPlayers;
    private Set<Integer> bestNumberOfPlayers;
    private Long addedToGameGroupDate;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public RatingModel getRating() {
        return rating;
    }

    public void setRating(RatingModel rating) {
        this.rating = rating;
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

    public TagWrapper getTags() {
        return tags;
    }

    public void setTags(TagWrapper tags) {
        this.tags = tags;
    }

    public Set<Integer> getRecommendedNumberOfPlayers() {
        return recommendedNumberOfPlayers;
    }

    public void setRecommendedNumberOfPlayers(Set<Integer> recommendedNumberOfPlayers) {
        this.recommendedNumberOfPlayers = recommendedNumberOfPlayers;
    }

    public Set<Integer> getBestNumberOfPlayers() {
        return bestNumberOfPlayers;
    }

    public void setBestNumberOfPlayers(Set<Integer> bestNumberOfPlayers) {
        this.bestNumberOfPlayers = bestNumberOfPlayers;
    }

    public Long getAddedToGameGroupDate() {
        return addedToGameGroupDate;
    }

    public void setAddedToGameGroupDate(Long addedToGameGroupDate) {
        this.addedToGameGroupDate = addedToGameGroupDate;
    }
}
