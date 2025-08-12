package de.oetting.bgp.gamegroup.controller;

import de.oetting.bgp.controller.IdWrapper;
import de.oetting.bgp.entities.Rating;
import de.oetting.bgp.exceptions.ConflictException;
import de.oetting.bgp.game.entity.Game;
import de.oetting.bgp.game.model.PlayerTagModel;
import de.oetting.bgp.game.model.RatedGameModel;
import de.oetting.bgp.game.model.TagModel;
import de.oetting.bgp.game.model.TagWrapper;
import de.oetting.bgp.game.repository.GameRepository;
import de.oetting.bgp.gamegroup.model.CreateGameGroupRequest;
import de.oetting.bgp.gamegroup.model.GameGroupMemberModel;
import de.oetting.bgp.gamegroup.model.GameGroupModel;
import de.oetting.bgp.gamegroup.model.GameGroupModelMapper;
import de.oetting.bgp.gamegroup.persistence.Game2GameGroupId;
import de.oetting.bgp.gamegroup.persistence.Game2GameGroupRelation;
import de.oetting.bgp.gamegroup.persistence.Game2GameGroupRepository;
import de.oetting.bgp.gamegroup.persistence.GameGroupEntity;
import de.oetting.bgp.gamegroup.persistence.GameGroupMemberType;
import de.oetting.bgp.gamegroup.persistence.GameGroupRepository;
import de.oetting.bgp.gamegroup.persistence.GameGroupType;
import de.oetting.bgp.gamegroup.service.GameGroupService;
import de.oetting.bgp.player.persistence.PlayerRepository;
import de.oetting.bgp.player.service.PlayerService;
import de.oetting.bgp.rating.controller.RatingService;
import de.oetting.bgp.repositories.RatingRepository;
import de.oetting.bgp.security.Role;
import de.oetting.bgp.tags.entity.GameGroupTagEntity;
import de.oetting.bgp.tags.entity.PlayerTagEntity;
import de.oetting.bgp.tags.entity.TagEntity;
import de.oetting.bgp.tags.entity.TagType;
import de.oetting.bgp.tags.repository.GameGroupTagRepository;
import de.oetting.bgp.tags.repository.PlayerTagRepository;
import de.oetting.bgp.tags.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/gameGroups")
public class GameGroupController {

    @Autowired
    private GameGroupRepository gameGroupRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private GameGroupService gameGroupService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private GameGroupTagRepository gameGroupTagRepository;

    @Autowired
    private PlayerTagRepository playerTagRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private Game2GameGroupRepository game2GameGroupRepository;

    @Transactional
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public GameGroupModel createGameGroup(@RequestBody CreateGameGroupRequest request) {
        GameGroupEntity savedEntity = gameGroupService.createGameGroup(request);

        return GameGroupModelMapper.map(savedEntity);
    }

    @Transactional
    @DeleteMapping(path = "/{gameGroupId}")
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    public void deleteGameGroup(@PathVariable("gameGroupId") long gameGroupId) {
        gameGroupService.deleteGameGroup(gameGroupId);
    }

    @GetMapping
    @Transactional
    public List<GameGroupModel> getGameGroups() {
        return gameGroupRepository.findByType(GameGroupType.PUBLIC).stream()
                .map(GameGroupModelMapper::map)
                .toList();
    }

    @GetMapping(path = "/{gameGroupId}")
    @Transactional
    public GameGroupModel getGameGroup(@PathVariable("gameGroupId") long gameGroupId) {
        return gameGroupRepository.findById(gameGroupId)
                .map(GameGroupModelMapper::map)
                .orElseThrow(() -> new NoSuchElementException("Group does not exist"));
    }

    @Transactional
    @PostMapping(path = "/{gameGroupId}/players")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addPlayerById(@RequestBody IdWrapper playerId, @PathVariable("gameGroupId") long gameGroupId) {
        gameGroupService.addPlayerById(playerId.getId(), gameGroupId);
    }

    @GetMapping(path = "/{gameGroupId}/players")
    @Transactional
    public Collection<GameGroupMemberModel> listPlayers(@PathVariable("gameGroupId") long gameGroupId) {
        return gameGroupRepository.findById(gameGroupId).orElseThrow()
                .getPlayers().stream()
                .sorted(Comparator.comparing(membership -> membership.getPlayer().getName()))
                .map(GameGroupModelMapper::map)
                .toList();
    }

    @Transactional
    @PostMapping(path = "/{gameGroupId}/games")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addGameById(@RequestBody IdWrapper gameId, @PathVariable("gameGroupId") long gameGroupId) {
        gameGroupService.addPlayedGameById(gameId.getId(), gameGroupId);
    }

    @GetMapping(path = "/{gameGroupId}/games")
    @Transactional
    public Collection<RatedGameModel> listGamesInGroup(@PathVariable("gameGroupId") long gameGroupId) {
        var playedGames = gameGroupRepository.findById(gameGroupId).orElseThrow().getGames();
        List<Rating> ratings = ratingRepository.findByGameGroupId(gameGroupId);
        var gameGroupTags = gameGroupTagRepository.findByGameGroupId(gameGroupId);
        var playerTags = playerTagRepository.findByGameGroupId(gameGroupId);

        return playedGames.stream()
                .map(game -> map(ratings, game, gameGroupTags, playerTags))
                .toList();
    }

    @GetMapping(path = "/{gameGroupId}/games/{gameId}")
    @Transactional
    public RatedGameModel getGame(@PathVariable("gameGroupId") long gameGroupId, @PathVariable("gameId") long gameId) {
        var game = game2GameGroupRepository.findById(new Game2GameGroupId(gameId, gameGroupId)).orElseThrow();
        List<Rating> ratings = ratingRepository.findByGameGroupIdAndGameId(gameGroupId, gameId);
        var gameGroupTags = gameGroupTagRepository.findByGameGroupId(gameGroupId);
        var playerTags = playerTagRepository.findByGameGroupId(gameGroupId);

        return map(ratings, game, gameGroupTags, playerTags);
    }

    @Transactional
    @PostMapping(path = "/{gameGroupId}/{gameId}/gameGroupTag")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addGameGroupTagById(@RequestBody IdWrapper gameTagId, @PathVariable("gameGroupId") long gameGroupId, @PathVariable("gameId") long gameId) {
        gameGroupService.checkUserIsPartOfGroup(gameGroupId);
        Game game = gameRepository.findById(gameId).orElseThrow();
        TagEntity tag = tagRepository.findById(gameTagId.getId()).orElseThrow();
        GameGroupEntity gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();
        if (!TagType.GAME_GROUP.equals(tag.getType())) {
            throw new ConflictException("Only game group tags can be added to game groups");
        }
        GameGroupTagEntity gameGroupTag = new GameGroupTagEntity(game, tag, gameGroup);
        gameGroup.addGameGroupTag(gameGroupTagRepository.save(gameGroupTag));
    }

    @Transactional
    @DeleteMapping(path = "/{gameGroupId}/{gameId}/gameGroupTag/{tagId}")
    public void removeGameGroupTagById(@PathVariable("gameGroupId") long gameGroupId, @PathVariable("gameId") long gameId, @PathVariable("tagId") long tagId) {
        gameGroupService.checkUserIsPartOfGroup(gameGroupId);
        gameGroupTagRepository.findByGameGroupIdAndGameIdAndTagId(gameGroupId, gameId, tagId)
                .ifPresent(gameGroupTagRepository::delete);
    }

    @Transactional
    @PostMapping(path = "/{gameGroupId}/{gameId}/{playerId}/playerTag")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addPlayerTagById(@RequestBody IdWrapper gameTagId,
                                 @PathVariable("gameGroupId") long gameGroupId,
                                 @PathVariable("gameId") long gameId,
                                 @PathVariable("playerId") long playerId
    ) {
        gameGroupService.checkUserIsPartOfGroup(gameGroupId);
        playerService.checkCurrentPlayerId(playerId);

        var game = gameRepository.findById(gameId).orElseThrow();
        var tag = tagRepository.findById(gameTagId.getId()).orElseThrow();
        var gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();
        var player = playerRepository.findById(playerId).orElseThrow();
        if (!TagType.PLAYER.equals(tag.getType())) {
            throw new ConflictException("Only player tags can be added here");
        }
        var playerTag = new PlayerTagEntity(game, tag, gameGroup, player);
        gameGroup.addPlayerTag(playerTagRepository.save(playerTag));
    }

    @Transactional
    @DeleteMapping(path = "/{gameGroupId}/{gameId}/{playerId}/playerTag/{tagId}")
    public void removePlayerTagById(
            @PathVariable("gameGroupId") long gameGroupId,
            @PathVariable("gameId") long gameId,
            @PathVariable("tagId") long tagId,
            @PathVariable("playerId") long playerId
    ) {
        gameGroupService.checkUserIsPartOfGroup(gameGroupId);
        playerService.checkCurrentPlayerId(playerId);
        playerTagRepository.findByGameGroupIdAndGameIdAndTagIdAndPlayerId(gameGroupId, gameId, tagId, playerId)
                .ifPresent(playerTagRepository::delete);
    }

    @Transactional
    @PutMapping(path = "/{gameGroupId}/{playerId}/{type}")
    public void updateMembershipType(@PathVariable("gameGroupId") long gameGroupId,
                                     @PathVariable("playerId") long playerId,
                                     @PathVariable("type") GameGroupMemberType type
    ) {
        gameGroupService.checkUserIsAdminOrOwnerInGroupOrGlobalAdmin(gameGroupId);
        if (type == GameGroupMemberType.OWNER) {
            gameGroupService.checkUserIsOwnerInGroupOrGlobalAdmin(gameGroupId);
        }
        var membership = gameGroupRepository.playerAssignedToGameGroup(playerId, gameGroupId);
        if (membership.isEmpty()) {
            throw new NoSuchElementException("Player is not member of group");
        }

        membership.get().setType(type);
    }

    private RatedGameModel map(List<Rating> ratings, Game2GameGroupRelation game2GameGroupRelation, List<GameGroupTagEntity> gameGroupTags, List<PlayerTagEntity> playerTags) {
        var game = game2GameGroupRelation.getGame();
        RatedGameModel ratedGame = new RatedGameModel();
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
        var tagWrapper = new TagWrapper();
        tagWrapper.setGlobal(game.getGlobalTags().stream().map(this::map).toList());
        tagWrapper.setGroup(gameGroupTags.stream()
                .filter(gameGroupTag -> gameGroupTag.getGame().getId().equals(game.getId()))
                .map(GameGroupTagEntity::getTag).map(this::map).toList());
        tagWrapper.setPlayer(playerTags.stream()
                .filter(playerTag -> playerTag.getGame().getId().equals(game.getId()))
                .map(this::map).toList());
        ratedGame.setTags(tagWrapper);
        ratedGame.setRecommendedNumberOfPlayers(game.getRecommendedNumberOfPlayers());
        ratedGame.setBestNumberOfPlayers(game.getBestNumberOfPlayers());
        ratedGame.setAddedToGameGroupDate(game2GameGroupRelation.getAddedToGroupTime() != null ? game2GameGroupRelation.getAddedToGroupTime().toInstant(ZoneOffset.UTC).toEpochMilli() : null);
        return ratedGame;
    }

    private TagModel map(TagEntity tag) {
        TagModel model = new TagModel();
        model.setDescription(tag.getDescription());
        model.setId(tag.getId());
        return model;
    }

    private PlayerTagModel map(PlayerTagEntity tag) {
        var model = new PlayerTagModel();
        model.setDescription(tag.getTag().getDescription());
        model.setId(tag.getTag().getId());
        model.setPlayerId(tag.getPlayer().getId());
        return model;
    }

}
