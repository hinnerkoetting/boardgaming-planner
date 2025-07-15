package de.oetting.bgp.game.controller;

import de.oetting.bgp.game.model.GameStatisticsModel;
import de.oetting.bgp.gamingevent.entity.GamingEventGameRepository;
import de.oetting.bgp.gamingevent.entity.GamingEventParticipantsEntity;
import de.oetting.bgp.gamingevent.entity.GamingEventRepository;
import de.oetting.bgp.infrastructure.CurrentUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/games/{gameId}")
public class GameStatisticsController {

    @Autowired
    private GamingEventGameRepository gamingEventGameRepository;

    @Autowired
    private GamingEventRepository gamingEventRepository;

    @GetMapping(path="/statistics")
    @Transactional
    public GameStatisticsModel loadGameStatistics(@PathVariable("gameId") long gameId) {
        var currentPlayerId = CurrentUser.getCurrentPlayerId();
        var model = new GameStatisticsModel();
        Optional<Long> lastPlayDate = gamingEventGameRepository.findByGameIdAndLastByOrderByGamingEventStart(gameId, currentPlayerId)
                .map(eventGame -> eventGame.getGamingEvent().getStart().toInstant().toEpochMilli());
        model.setLastPlayed(lastPlayDate.orElse(null));
        model.setPlayedNumberOfTimes(gamingEventGameRepository.countByGameId(gameId, currentPlayerId));
        var gamingEvents = gamingEventRepository.findGamingEventsWithGame(gameId, currentPlayerId);
        model.setPlayDates(gamingEvents.stream().map(event -> event.getStart().toInstant().toEpochMilli()).toList());
        model.setNumberOfTimesPlayedByPlayers(gamingEvents.stream()
                .flatMap(event -> event.getParticipants().stream())
                .collect(Collectors.groupingBy(p -> p.getParticipant().getName(), Collectors.counting())));
        return model;
    }
}
