package de.oetting.wwp.rating.controller;

import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.entities.Rating;
import de.oetting.wwp.exceptions.UnprocessableEntityException;
import de.oetting.wwp.game.entity.Game;
import de.oetting.wwp.game.repository.GameRepository;
import de.oetting.wwp.infrastructure.CurrentUser;
import de.oetting.wwp.player.PlayerRepository;
import de.oetting.wwp.rating.RatingModel;
import de.oetting.wwp.rating.RatingRequest;
import de.oetting.wwp.repositories.GameGroupRepository;
import de.oetting.wwp.repositories.RatingRepository;
import de.oetting.wwp.service.events.GameGroupEvent;
import de.oetting.wwp.service.events.GameGroupEventService;
import de.oetting.wwp.service.events.GameGroupEventType;
import de.oetting.wwp.service.events.SseEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameGroupRepository gameGroupRepository;
    @Autowired
    private GameGroupEventService gameGroupEventService;

    public RatingModel updateRating(RatingRequest request) {
        checkUserIsPartOfGroup(request.getGameGroupId());
        var optionalRating = ratingRepository.findByGameGroupIdAndPlayerIdAndGameId(request.getGameGroupId(), request.getPlayerId(), request.getGameId());

        if (optionalRating.isPresent()) {
            optionalRating.get().setRating(request.getRating());
        } else {
            createNewRating(request);
        }

        var game = gameRepository.findById(request.getGameId()).orElseThrow();
        gameGroupEventService.ratingUpdated(request.getGameGroupId(), game, request.getRating());

        return computeRating(request);
    }

    public RatingModel deleteRating(RatingRequest request) {
        checkUserIsPartOfGroup(request.getGameGroupId());
        ratingRepository.deleteByGameGroupIdAndPlayerIdAndGameId(request.getGameGroupId(), request.getPlayerId(), request.getGameId());
        return computeRating(request);
    }

    private void checkUserIsPartOfGroup(long gameGroupId){
        var myPlayer = findMyPlayer();
        if (gameGroupRepository.playerAssignedToGameGroup(myPlayer.getId(), gameGroupId).isEmpty()) {
            throw new UnprocessableEntityException("You cannot rate games in groups that you did not join");
        }
    }

    private Player findMyPlayer() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playerRepository.findByName(username).orElseThrow(() -> new NoSuchElementException("Player not found"));
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
        return computeRating(game, ratings);
    }

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
        ratingModel.setNumberOfVotes(gameRatings.size());
        return ratingModel;
    }

}
