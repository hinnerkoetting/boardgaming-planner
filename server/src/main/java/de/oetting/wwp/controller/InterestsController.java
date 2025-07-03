package de.oetting.wwp.controller;

import de.oetting.wwp.controller.model.InterestRequest;
import de.oetting.wwp.controller.model.InterestResponse;
import de.oetting.wwp.entities.Game;
import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.entities.Interest;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.repositories.GameGroupRepository;
import de.oetting.wwp.repositories.GameRepository;
import de.oetting.wwp.repositories.InterestRepository;
import de.oetting.wwp.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/interests")
public class InterestsController {

    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameGroupRepository gameGroupRepository;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addInterest(@RequestBody InterestRequest request) {
        Player player = playerRepository.findById(request.getPlayerId()).orElseThrow(() -> new NoSuchElementException("Player not found"));
        Game game = gameRepository.findById(request.getGameId()).orElseThrow(() -> new NoSuchElementException("Game not found"));
        GameGroup gameGroup = gameGroupRepository.findById(request.getGameGroupId()).orElseThrow(() -> new NoSuchElementException("GameGroup not found"));
        Interest interest =new Interest();
        interest.setGame(game);
        interest.setGameGroup(gameGroup);
        interest.setPlayer(player);
        interest.setRating(request.getRating());
        interestRepository.save(interest);
    }

    @GetMapping("/gameGroup/{gameGroupId}/players/{playerId}")
    public List<InterestResponse> listMyInterests(@PathVariable("gameGroupId") long gameGroupId,@PathVariable("playerId") long playerId) {
        List<Interest> interests = interestRepository.findByGameGroupIdAndPlayerId(gameGroupId, playerId);
        return interests.stream().map(interest -> {
            var response = new InterestResponse();
            response.setGameGroupId(interest.getGameGroup().getId());
            response.setGameId(interest.getGame().getId());
            response.setPlayerId(interest.getPlayer().getId());
            response.setRating(interest.getRating());
            return response;
        }).toList();
    }
}
