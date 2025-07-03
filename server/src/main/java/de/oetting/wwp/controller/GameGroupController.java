package de.oetting.wwp.controller;

import de.oetting.wwp.controller.model.RatedGameModel;
import de.oetting.wwp.entities.Game;
import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.entities.Rating;
import de.oetting.wwp.exceptions.ConflictException;
import de.oetting.wwp.repositories.GameGroupRepository;
import de.oetting.wwp.repositories.GameRepository;
import de.oetting.wwp.player.PlayerRepository;
import de.oetting.wwp.repositories.RatingRepository;
import de.oetting.wwp.service.RatingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "api/gameGroups/{gameGroupId}/")
public class GameGroupController {

    @Autowired
    private GameGroupRepository gameGroupRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingService ratingService;

    @Transactional
    @PostMapping(path = "players")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addPlayerById(@RequestBody IdWrapper playerId, @PathVariable("gameGroupId") long gameGroupId) {
        if (gameGroupRepository.playerAssignedToGameGroup(playerId.getId(), gameGroupId).isPresent()) {
            throw new ConflictException("Player already exists");
        }
        Player player = playerRepository.findById(playerId.getId()).orElseThrow();
        GameGroup gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();
        gameGroup.addPlayer(player);
    }

    @GetMapping(path = "players")
    public Collection<Player> listPlayers(@PathVariable("gameGroupId") long gameGroupId) {
        return gameGroupRepository.findById(gameGroupId).orElseThrow().getPlayers();
    }

    @Transactional
    @PostMapping(path = "games")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addPlayedGameById(@RequestBody IdWrapper gameId, @PathVariable("gameGroupId") long gameGroupId) {
        var game = gameRepository.findById(gameId.getId()).orElseThrow();
        var gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();
        gameGroup.addGame(game);
    }

    @GetMapping(path = "games")
    public Collection<RatedGameModel> listPlayedGames(@PathVariable("gameGroupId") long gameGroupId) {
        Collection<Game> playedGames = gameGroupRepository.findById(gameGroupId).orElseThrow().getGames();
        List<Rating> ratings = ratingRepository.findByGameGroupId(gameGroupId);

        return playedGames.stream().map(game ->
        {
            RatedGameModel ratedGame =new RatedGameModel();
            ratedGame.setDescription(game.getDescription());
            ratedGame.setThumbnailUrl(game.getThumbnailUrl());
            ratedGame.setId(game.getId());
            ratedGame.setName(game.getName());
            ratedGame.setRating(ratingService.computeRating(game, ratings));
            return ratedGame;
        }).toList();

    }

}
