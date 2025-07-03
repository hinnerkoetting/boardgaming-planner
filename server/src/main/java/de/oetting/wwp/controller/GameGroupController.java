package de.oetting.wwp.controller;

import de.oetting.wwp.entities.Game;
import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.exceptions.ConflictException;
import de.oetting.wwp.repositories.GameGroupRepository;
import de.oetting.wwp.repositories.GameRepository;
import de.oetting.wwp.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "api/gameGroups/{gameGroupId}/")
public class GameGroupController {

    @Autowired
    private GameGroupRepository gameGroupRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

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
    @PostMapping(path = "playedGames")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addPlayedGameById(@RequestBody IdWrapper gameId, @PathVariable("gameGroupId") long gameGroupId) {
        var game = gameRepository.findById(gameId.getId()).orElseThrow();
        var gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();
        gameGroup.addGame(game);
    }

    @GetMapping(path = "playedGames")
    public Collection<Game> listPlayedGames(@PathVariable("gameGroupId") long gameGroupId) {
        return gameGroupRepository.findById(gameGroupId).orElseThrow().getPlayedGames();
    }

}
