package de.oetting.wwp.controller;

import de.oetting.wwp.controller.model.RatingRequest;
import de.oetting.wwp.controller.model.RatingResponse;
import de.oetting.wwp.entities.Game;
import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.entities.Rating;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.repositories.GameGroupRepository;
import de.oetting.wwp.repositories.GameRepository;
import de.oetting.wwp.repositories.RatingRepository;
import de.oetting.wwp.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PutMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional
    public void updateInterest(@RequestBody RatingRequest request) {
        var optionalRating = ratingRepository.findByGameGroupIdAndPlayerIdAndGameId(request.getGameGroupId(), request.getPlayerId(), request.getGameId());

        if (optionalRating.isPresent()) {
             optionalRating.get().setRating(request.getRating());
            return;
        }
        Player player = playerRepository.findById(request.getPlayerId()).orElseThrow(() -> new NoSuchElementException("Player not found"));
        Game game = gameRepository.findById(request.getGameId()).orElseThrow(() -> new NoSuchElementException("Game not found"));
        GameGroup gameGroup = gameGroupRepository.findById(request.getGameGroupId()).orElseThrow(() -> new NoSuchElementException("GameGroup not found"));
        Rating rating =new Rating();
        rating.setGame(game);
        rating.setGameGroup(gameGroup);
        rating.setPlayer(player);
        rating.setRating(request.getRating());
        ratingRepository.save(rating);
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public void deleteRating(@RequestBody RatingRequest request) {
        ratingRepository.deleteByGameGroupIdAndPlayerIdAndGameId(request.getGameGroupId(), request.getPlayerId(), request.getGameId());
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
