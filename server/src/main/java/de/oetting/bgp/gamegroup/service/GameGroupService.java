package de.oetting.bgp.gamegroup.service;

import de.oetting.bgp.entities.Player;
import de.oetting.bgp.exceptions.ConflictException;
import de.oetting.bgp.exceptions.UnprocessableEntityException;
import de.oetting.bgp.game.repository.GameRepository;
import de.oetting.bgp.gamegroup.model.CreateGameGroupRequest;
import de.oetting.bgp.gamegroup.persistence.*;
import de.oetting.bgp.player.persistence.PlayerRepository;
import de.oetting.bgp.repositories.RatingRepository;
import de.oetting.bgp.service.events.GameGroupEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GameGroupService {

    @Autowired
    private GameGroupRepository gameGroupRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameGroupEventService gameGroupEventService;

    @Autowired
    private Game2GameGroupRepository game2GameGroupRepository;

    @Autowired
    private RatingRepository ratingRepository;

    public GameGroup createGameGroup(CreateGameGroupRequest request) {
        if (gameGroupRepository.existsByName(request.getName())) {
            throw new ConflictException("Group already exists");
        }
        GameGroup groupEntity = new GameGroup();
        groupEntity.setName(request.getName());
        groupEntity.setType(request.getType());
        return gameGroupRepository.save(groupEntity);
    }

    public void addPlayerById(long playerId, long gameGroupId) {
        if (gameGroupRepository.playerAssignedToGameGroup(playerId, gameGroupId).isPresent()) {
            throw new ConflictException("Player already exists");
        }
        Player player = playerRepository.findById(playerId).orElseThrow();
        GameGroup gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();
        if (gameGroup.getType() == GameGroupType.PERSONAL) {
            throw new ConflictException("Cannot join personal group");
        }
        gameGroup.addPlayer(player);

        gameGroupEventService.playerAdded(gameGroupId, player);
    }

    public void addPlayedGameById(long gameId, long gameGroupId) {
        var game = gameRepository.findById(gameId).orElseThrow();
        var gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();
        var relation = new Game2GameGroupRelation(gameId, gameGroupId);
        relation.setGameGroup(gameGroup);
        relation.setGame(game);
        relation.setAddedToGroupTime(LocalDateTime.now());
        var savedRelation = game2GameGroupRepository.save(relation);
        gameGroup.addGame(savedRelation);

        gameGroupEventService.gameAdded(gameGroupId, game);
    }

    public void checkUserIsPartOfGroup(long gameGroupId) {
        var myPlayer = findMyPlayer();
        if (gameGroupRepository.playerAssignedToGameGroup(myPlayer.getId(), gameGroupId).isEmpty()) {
            throw new UnprocessableEntityException("Only allowed if you are member of group");
        }
    }

    public Optional<GameGroup> find(long gameGroupId) {
        return gameGroupRepository.findById(gameGroupId);
    }

    public void deleteGameGroup(long gameGroupId) {
        game2GameGroupRepository.deleteByGameGroupId(gameGroupId);
        ratingRepository.deleteByGameGroupId(gameGroupId);
        gameGroupRepository.deleteById(gameGroupId);
    }

    private Player findMyPlayer() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playerRepository.findByName(username).orElseThrow(() -> new NoSuchElementException("Player not found"));
    }

    public GameGroup createPersonalCollection() {
        var myPlayer = findMyPlayer();
        var newPersonalCollection = new GameGroup();
        newPersonalCollection.setType(GameGroupType.PERSONAL);
        newPersonalCollection.setName(String.format("%s's personal collection", myPlayer.getName()));
        if (gameGroupRepository.existsByName(newPersonalCollection.getName())) {
            throw new ConflictException("Group already exists");
        }
        var storedPersonalCollection = gameGroupRepository.save(newPersonalCollection);
        storedPersonalCollection.setPlayers(Collections.singleton(myPlayer));
        return storedPersonalCollection;
    }

}
