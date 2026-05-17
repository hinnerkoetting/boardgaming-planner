package de.oetting.bgp.gamegroup.controller;

import de.oetting.bgp.gamegroup.model.GameGroupStatisticsModel;
import de.oetting.bgp.gamingevent.entity.GamingEventRepository;
import de.oetting.bgp.gamingevent.model.GamingEventModelMapper;
import de.oetting.bgp.infrastructure.CurrentUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping(path = "api/gameGroups/{gameGroupId}")
public class GameGroupStatisticsController {

    @Autowired
    private GamingEventRepository gamingEventRepository;

    @Autowired
    private GamingEventModelMapper gamingEventModelMapper;

    @Transactional
    @GetMapping(path = "/statistics")
    public GameGroupStatisticsModel loadGameGroupStatistics(
            @PathVariable("gameGroupId") long gameGroupId,
            @RequestParam(name = "startDate", required = false, defaultValue = "1970-01-01T22:00:00.000Z") ZonedDateTime startDate,
            @RequestParam(name = "endDate", required = false, defaultValue = "2099-12-31T22:00:00.000Z") ZonedDateTime endDate
    ) {
        var currentPlayerId = CurrentUser.getCurrentPlayerId();
        var model = new GameGroupStatisticsModel();
        model.setEvents(gamingEventRepository.findGamingEvents(gameGroupId, currentPlayerId, startDate, endDate).stream()
                .map(gamingEventModelMapper::map)
                .toList());
        return model;
    }
}
