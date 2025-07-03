package de.oetting.wwp.controller;

import de.oetting.wwp.entities.Game;
import de.oetting.wwp.repositories.GameGroupRepository;
import de.oetting.wwp.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @DeleteMapping(path = "/{gameId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteGameById(@PathVariable("gameId") long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        game.getGameGroups().forEach(gameGroup -> gameGroup.deleteGame(game));

        gameRepository.delete(game);
    }
}
