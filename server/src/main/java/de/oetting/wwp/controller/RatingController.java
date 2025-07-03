package de.oetting.wwp.controller;

import de.oetting.wwp.controller.model.RatingModel;
import de.oetting.wwp.controller.model.RatingRequest;
import de.oetting.wwp.controller.model.RatingResponse;
import de.oetting.wwp.entities.Game;
import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.entities.Rating;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.exceptions.UnprocessableEntityException;
import de.oetting.wwp.repositories.GameGroupRepository;
import de.oetting.wwp.repositories.GameRepository;
import de.oetting.wwp.repositories.RatingRepository;
import de.oetting.wwp.player.PlayerRepository;
import de.oetting.wwp.service.RatingService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
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
        checkUserIsPartOfGroup(request.getGameGroupId());
        var optionalRating = ratingRepository.findByGameGroupIdAndPlayerIdAndGameId(request.getGameGroupId(), request.getPlayerId(), request.getGameId());

        if (optionalRating.isPresent()) {
             optionalRating.get().setRating(request.getRating());
        } else {
            createNewRating(request);
        }

        return computeRating(request);
    }



    @DeleteMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public RatingModel deleteRating(@RequestBody RatingRequest request) {
        checkUserIsPartOfGroup(request.getGameGroupId());
        ratingRepository.deleteByGameGroupIdAndPlayerIdAndGameId(request.getGameGroupId(), request.getPlayerId(), request.getGameId());
        return computeRating(request);
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

    private void createNewRating(RatingRequest request) {
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

    private RatingModel computeRating(RatingRequest request) {
        Game game = gameRepository.findById(request.getGameId()).orElseThrow(() -> new NoSuchElementException("Game not found"));
        List<Rating> ratings = ratingRepository.findByGameGroupIdAndGameId(request.getGameGroupId(), request.getGameId());
        return ratingService.computeRating(game, ratings);
    }

    private void checkUserIsPartOfGroup(long gameGroupId){
        var myPlayer = findMyPlayer();
        if (gameGroupRepository.playerAssignedToGameGroup(myPlayer.getId(), gameGroupId).isEmpty()) {
            throw new UnprocessableEntityException("You cannot rate games in groups that you did not join");
        };
    }

    private Player findMyPlayer() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playerRepository.findByName(username).orElseThrow(() -> new NoSuchElementException("Player not found"));
    }
}
