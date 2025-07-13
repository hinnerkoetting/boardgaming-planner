package de.oetting.bgp.game.controller;

import de.oetting.bgp.game.model.GameStatisticsModel;
import de.oetting.bgp.gamingevent.entity.GamingEventGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "api/games/{gameId}")
public class GameStatisticsController {

    @Autowired
    private GamingEventGameRepository gamingEventGameRepository;

    @GetMapping(path="/statistics")
    public GameStatisticsModel loadGameStatistics(@PathVariable("gameId") long gameId) {
        var model = new GameStatisticsModel();
        Optional<Long> lastPlayDate = gamingEventGameRepository.findByGameIdAndLastByOrderByGamingEventStart(gameId)
                .map(eventGame -> eventGame.getGamingEvent().getStart().toInstant().toEpochMilli());
        model.setLastPlayed(lastPlayDate.orElse(null));
        model.setPlayedNumberOfTimes(gamingEventGameRepository.countByGameId(gameId));

        return model;
    }
}
