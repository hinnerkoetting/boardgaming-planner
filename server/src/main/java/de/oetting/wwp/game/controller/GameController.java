package de.oetting.wwp.game.controller;

import com.github.marcioos.bggclient.search.SearchException;
import de.oetting.wwp.entities.Game;
import de.oetting.wwp.exceptions.ConflictException;
import de.oetting.wwp.game.repository.GameRepository;
import de.oetting.wwp.repositories.RatingRepository;
import de.oetting.wwp.security.Role;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @GetMapping
    public Iterable<Game> findAll() {
        return gameRepository.findAll();
    }

    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@RequestBody Game game) {
        if (game.getId() != null) {
            throw new ConflictException("GameId must not be defined");
        }
        return gameRepository.save(game);
    }

    @Transactional
    @PutMapping(path = "/{gameId}")
    public void updateGame(@RequestBody Game game, @PathVariable("gameId") long gameId) {
        if (gameId != game.getId()) {
            throw new ConflictException("GameId is not correct");
        }
        Game existingGame = gameRepository.findById(gameId).orElseThrow();
        existingGame.setName(game.getName());
        existingGame.setDescription(game.getDescription());
        existingGame.setImageUrl(game.getImageUrl());
        existingGame.setThumbnailUrl(game.getThumbnailUrl());
    }

    @DeleteMapping(path = "/{gameId}")
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    @Transactional
    public void deleteGameById(@PathVariable("gameId") long gameId) {
        ratingRepository.deleteByGameId(gameId);
        Game game = gameRepository.findById(gameId).orElseThrow();
        game.getGameGroups().forEach(gameGroup -> gameGroup.deleteGame(game));

        gameRepository.delete(game);
    }

    @GetMapping("/search/{searchTerm}")
    public List<Game> searchGames(@PathVariable("searchTerm") String searchTerm) throws SearchException {
        return gameRepository.findByNameContaining(searchTerm);
    }
}
