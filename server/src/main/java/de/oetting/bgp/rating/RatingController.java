package de.oetting.bgp.rating;

import de.oetting.bgp.entities.Rating;
import de.oetting.bgp.game.repository.GameRepository;
import de.oetting.bgp.gamegroup.persistence.GameGroupRepository;
import de.oetting.bgp.player.PlayerRepository;
import de.oetting.bgp.rating.controller.RatingService;
import de.oetting.bgp.repositories.RatingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
