package de.oetting.wwp.rating;

import de.oetting.wwp.game.entity.Game;
import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.entities.Rating;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.exceptions.UnprocessableEntityException;
import de.oetting.wwp.rating.controller.RatingService;
import de.oetting.wwp.repositories.GameGroupRepository;
import de.oetting.wwp.game.repository.GameRepository;
import de.oetting.wwp.repositories.RatingRepository;
import de.oetting.wwp.player.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/ratings")
public class RatingController {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameGroupRepository gameGroupRepository;
    @Autowired
    private RatingService ratingService;

    @PutMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional
    public RatingModel updateRating(@RequestBody RatingRequest request) {
        return ratingService.updateRating(request);
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public RatingModel deleteRating(@RequestBody RatingRequest request) {
        return ratingService.deleteRating(request);
    }

    @GetMapping("/gameGroup/{gameGroupId}/player/{playerId}")
    public List<RatingResponse> listMyRatings(@PathVariable("gameGroupId") long gameGroupId, @PathVariable("playerId") long playerId) {
        List<Rating> ratings = ratingRepository.findByGameGroupIdAndPlayerId(gameGroupId, playerId);
        return ratings.stream().map(rating -> {
            var response = new RatingResponse();
            response.setGameGroupId(rating.getGameGroup().getId());
            response.setGameId(rating.getGame().getId());
            response.setPlayerId(rating.getPlayer().getId());
            response.setRating(rating.getRating());
            return response;
        }).toList();
    }

}
