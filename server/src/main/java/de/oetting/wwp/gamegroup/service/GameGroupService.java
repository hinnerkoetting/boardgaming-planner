package de.oetting.wwp.gamegroup.service;

import de.oetting.wwp.controller.IdWrapper;
import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.exceptions.ConflictException;
import de.oetting.wwp.game.repository.GameRepository;
import de.oetting.wwp.gamegroup.model.CreateGameGroupRequest;
import de.oetting.wwp.gamegroup.model.GameGroupModel;
import de.oetting.wwp.infrastructure.CurrentUser;
import de.oetting.wwp.player.PlayerRepository;
import de.oetting.wwp.repositories.GameGroupRepository;
import de.oetting.wwp.repositories.RatingRepository;
import de.oetting.wwp.service.RatingService;
import de.oetting.wwp.service.events.GameGroupEvent;
import de.oetting.wwp.service.events.GameGroupEventType;
import de.oetting.wwp.service.events.SseEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class GameGroupService {

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

    public GameGroup createGameGroup(CreateGameGroupRequest request){
        if (gameGroupRepository.existsByName(request.getName())) {
            throw new ConflictException("Group already exists");
        }
        GameGroup groupEntity = new GameGroup();
        groupEntity.setName(request.getName());
        return gameGroupRepository.save(groupEntity);
    }

    public void addPlayerById(long playerId, long gameGroupId) {
        if (gameGroupRepository.playerAssignedToGameGroup(playerId, gameGroupId).isPresent()) {
            throw new ConflictException("Player already exists");
        }
        Player player = playerRepository.findById(playerId).orElseThrow();
        GameGroup gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();
        gameGroup.addPlayer(player);

        var event = new GameGroupEvent(CurrentUser.getCurrentUsername(), gameGroupId, GameGroupEventType.PLAYER_ADDED, String.format("Player %s joined", player.getName()));
        sseEmitterService.onEventInGameGroup(event);
    }

    public void addPlayedGameById(long gameId, long gameGroupId) {
        var game = gameRepository.findById(gameId).orElseThrow();
        var gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();
        gameGroup.addGame(game);

        var event = new GameGroupEvent(CurrentUser.getCurrentUsername(), gameGroupId, GameGroupEventType.GAME_ADDED, String.format("Game %s was added", game.getName()));
        sseEmitterService.onEventInGameGroup(event);
    }
}
