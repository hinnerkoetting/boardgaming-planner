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
import jakarta.transaction.Transactional;
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

    @PutMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional
    public void updateInterest(@RequestBody InterestRequest request) {
        var optionalInterest = interestRepository.findByGameGroupIdAndPlayerIdAndGameId(request.getGameGroupId(), request.getPlayerId(), request.getGameId());

        if (optionalInterest.isPresent()) {
            var interest = optionalInterest.get();
            interest.setRating(request.getRating());
            return;
        }
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

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public void deleteInterest(@RequestBody InterestRequest request) {
        interestRepository.deleteByGameGroupIdAndPlayerIdAndGameId(request.getGameGroupId(), request.getPlayerId(), request.getGameId());
    }

    @GetMapping("/gameGroup/{gameGroupId}/player/{playerId}")
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
