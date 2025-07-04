package de.oetting.bgp.gamegroup.service;

import de.oetting.bgp.entities.GameGroup;
import de.oetting.bgp.entities.Player;
import de.oetting.bgp.exceptions.ConflictException;
import de.oetting.bgp.exceptions.UnprocessableEntityException;
import de.oetting.bgp.game.repository.GameRepository;
import de.oetting.bgp.gamegroup.model.CreateGameGroupRequest;
import de.oetting.bgp.player.PlayerRepository;
import de.oetting.bgp.repositories.GameGroupRepository;
import de.oetting.bgp.service.events.GameGroupEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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

        gameGroupEventService.playerAdded(gameGroupId, player);
    }

    public void addPlayedGameById(long gameId, long gameGroupId) {
        var game = gameRepository.findById(gameId).orElseThrow();
        var gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();
        gameGroup.addGame(game);

        gameGroupEventService.gameAdded(gameGroupId, game);
    }

    public void checkUserIsPartOfGroup(long gameGroupId){
        var myPlayer = findMyPlayer();
        if (gameGroupRepository.playerAssignedToGameGroup(myPlayer.getId(), gameGroupId).isEmpty()) {
            throw new UnprocessableEntityException("Only allowed if you are member of group");
        }
    }

    private Player findMyPlayer() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playerRepository.findByName(username).orElseThrow(() -> new NoSuchElementException("Player not found"));
    }
}
