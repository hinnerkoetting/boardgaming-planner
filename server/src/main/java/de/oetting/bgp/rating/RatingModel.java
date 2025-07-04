package de.oetting.bgp.rating;

import java.math.BigDecimal;

public class RatingModel {

    private Integer myRating;
    private int numberOfVotes;
    private BigDecimal averageRating;
    private boolean existsVeto;

    public Integer getMyRating() {
        return myRating;
    }

    public void setMyRating(Integer myRating) {
        this.myRating = myRating;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public boolean isExistsVeto() {
        return existsVeto;
    }

    public void setExistsVeto(boolean existsVeto) {
        this.existsVeto = existsVeto;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }
}
