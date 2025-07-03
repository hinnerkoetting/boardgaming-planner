package de.oetting.wwp.service;

import de.oetting.wwp.controller.model.RatingModel;
import de.oetting.wwp.entities.Game;
import de.oetting.wwp.entities.Rating;
import de.oetting.wwp.infrastructure.CurrentUser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingService {

    public RatingModel computeRating(Game game, List<Rating> ratings) {
        List<Rating> gameRatings = ratings.stream().filter(rating -> rating.getGame().equals(game)).toList();
        var ratingModel  =new RatingModel();
        Optional<Rating> myRating = gameRatings.stream()
                .filter(rating -> rating.getPlayer().getName().equals(CurrentUser.getCurrentUsername()))
                .findAny();
        ratingModel.setMyRating(myRating.map(Rating::getRating).orElse(null));
        double average = gameRatings.stream()
                .collect(Collectors.averagingDouble(Rating::getRating));
        ratingModel.setAverageRating(gameRatings.isEmpty() ? null: new BigDecimal(average));
        ratingModel.setExistsVeto(gameRatings.stream().anyMatch(rating -> rating.getRating() == 0));
        return ratingModel;
    }
}
