package de.oetting.bgp.gamegroup.controller;

import de.oetting.bgp.game.model.GameStatisticsModel;
import de.oetting.bgp.gamegroup.model.GameGroupStatisticsModel;
import de.oetting.bgp.gamingevent.entity.GamingEventGameRepository;
import de.oetting.bgp.gamingevent.entity.GamingEventRepository;
import de.oetting.bgp.infrastructure.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "api/gameGroups/{gameGroupId}")
public class GameGroupStatisticsController {

    @Autowired
    private GamingEventRepository gamingEventRepository;

    @GetMapping(path="/statistics")
    public GameGroupStatisticsModel loadGameGroupStatistics(@PathVariable("gameGroupId") long gameGroupId) {
        var currentPlayerId = CurrentUser.getCurrentPlayerId();
        var model = new GameGroupStatisticsModel();
        model.setPlayDates(gamingEventRepository.findGamingEvents(gameGroupId, currentPlayerId).stream().map(event -> event.getStart().toInstant().toEpochMilli()).toList());
        return model;
    }
}
