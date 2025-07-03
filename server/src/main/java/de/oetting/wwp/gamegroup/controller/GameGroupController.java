package de.oetting.wwp.gamegroup.controller;

import de.oetting.wwp.controller.IdWrapper;
import de.oetting.wwp.game.model.RatedGameModel;
import de.oetting.wwp.game.entity.Game;
import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.entities.Rating;
import de.oetting.wwp.exceptions.ConflictException;
import de.oetting.wwp.game.model.TagModel;
import de.oetting.wwp.gamegroup.model.CreateGameGroupRequest;
import de.oetting.wwp.gamegroup.model.GameGroupModel;
import de.oetting.wwp.gamegroup.service.GameGroupService;
import de.oetting.wwp.infrastructure.CurrentUser;
import de.oetting.wwp.repositories.GameGroupRepository;
import de.oetting.wwp.game.repository.GameRepository;
import de.oetting.wwp.player.PlayerRepository;
import de.oetting.wwp.repositories.RatingRepository;
import de.oetting.wwp.security.Role;
import de.oetting.wwp.service.RatingService;
import de.oetting.wwp.service.events.GameGroupEvent;
import de.oetting.wwp.service.events.GameGroupEventType;
import de.oetting.wwp.service.events.SseEmitterService;
import de.oetting.wwp.tags.entity.TagEntity;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "api/gameGroups")
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

    @Autowired
    private SseEmitterService sseEmitterService;

    @Autowired
    private GameGroupService gameGroupService;

    @Transactional
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public GameGroupModel createGameGroup(@RequestBody CreateGameGroupRequest request){
        GameGroup savedEntity = gameGroupService.createGameGroup(request);

        return map(savedEntity);
    }

    @Transactional
    @DeleteMapping(path = "/{gameGroupId}")
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    public void deleteGameGroup(@PathVariable("gameGroupId") long gameGroupId){
        gameGroupRepository.deleteById(gameGroupId);
    }

    @GetMapping
    public List<GameGroupModel> getGameGroups(){
        return StreamSupport.stream(gameGroupRepository.findAll().spliterator(), false)
                .map(this::map)
                .toList();
    }

    @GetMapping(path = "/{gameGroupId}")
    public GameGroupModel getGameGroup(@PathVariable("gameGroupId") long gameGroupId){
       return gameGroupRepository.findById(gameGroupId)
               .map(this::map)
               .orElseThrow(() -> new NoSuchElementException("Group does not exist"));
    }

    @Transactional
    @PostMapping(path = "/{gameGroupId}/players")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addPlayerById(@RequestBody IdWrapper playerId, @PathVariable("gameGroupId") long gameGroupId) {
       gameGroupService.addPlayerById(playerId.getId(), gameGroupId);
    }

    @GetMapping(path = "/{gameGroupId}/players")
    public Collection<Player> listPlayers(@PathVariable("gameGroupId") long gameGroupId) {
        return gameGroupRepository.findById(gameGroupId).orElseThrow().getPlayers();
    }

    @Transactional
    @PostMapping(path = "/{gameGroupId}/games")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addPlayedGameById(@RequestBody IdWrapper gameId, @PathVariable("gameGroupId") long gameGroupId) {
        gameGroupService.addPlayedGameById(gameId.getId(), gameGroupId);
    }

    @GetMapping(path = "/{gameGroupId}/games")
    public Collection<RatedGameModel> listPlayedGames(@PathVariable("gameGroupId") long gameGroupId) {
        Collection<Game> playedGames = gameGroupRepository.findById(gameGroupId).orElseThrow().getGames();
        List<Rating> ratings = ratingRepository.findByGameGroupId(gameGroupId);

        return playedGames.stream()
                .map(game -> map(ratings, game))
                .toList();
    }

    private RatedGameModel map(List<Rating> ratings, Game game) {
        RatedGameModel ratedGame =new RatedGameModel();
        ratedGame.setDescription(game.getDescription());
        ratedGame.setThumbnailUrl(game.getThumbnailUrl());
        ratedGame.setImageUrl(game.getImageUrl());
        ratedGame.setId(game.getId());
        ratedGame.setName(game.getName());
        ratedGame.setRating(ratingService.computeRating(game, ratings));
        ratedGame.setMaxPlayers(game.getMaxPlayers());
        ratedGame.setMinPlayers(game.getMinPlayers());
        ratedGame.setUrl(game.getUrl());
        ratedGame.setPlayingTimeMinutes(game.getPlayingTimeMinutes());
        ratedGame.setTags(game.getGlobalTags().stream().map(this::map).collect(Collectors.toList()));
        ratedGame.setRecommendedNumberOfPlayers(game.getRecommendedNumberOfPlayers());
        ratedGame.setBestNumberOfPlayers(game.getBestNumberOfPlayers());
        return ratedGame;
    }

    private TagModel map(TagEntity tag) {
        TagModel model = new TagModel();
        model.setDescription(tag.getDescription());
        model.setId(tag.getId());
        return model;
    }

    @NotNull
    private GameGroupModel map(GameGroup savedEntity) {
        GameGroupModel model = new GameGroupModel();
        model.setId(savedEntity.getId());
        model.setName(savedEntity.getName());
        return model;
    }
}
