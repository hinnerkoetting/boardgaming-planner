package de.oetting.bgp.gamingevent.model;

import de.oetting.bgp.game.model.GameConverter;
import de.oetting.bgp.gamegroup.service.GameGroupService;
import de.oetting.bgp.gamingevent.entity.GamingEventEntity;
import de.oetting.bgp.gamingevent.entity.GamingEventGameEntity;
import de.oetting.bgp.gamingevent.entity.GamingEventParticipantsEntity;
import de.oetting.bgp.player.controller.PlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;

@Service
public class GamingEventModelMapper {

    @Autowired
    private GameGroupService gameGroupService;

    public GamingEventModel map(GamingEventEntity entity) {
        var model = new GamingEventModel();
        model.setDescription(entity.getDescription());
        model.setStart(entity.getStart().toInstant().toEpochMilli());
        model.setId(entity.getId());
        model.setGames(entity.getGames() == null ? Collections.emptyList() : entity.getGames().stream().map(this::map).toList());
        model.setParticipants(entity.getParticipants() == null ? Collections.emptyList() : entity.getParticipants().stream().map(this::map).toList());
        model.setSchedule(entity.getSchedule());
        model.setParentEventId(entity.getParent() != null ? entity.getParent().getId() : null);
        return model;
    }

    public GamingEventGameModel map(GamingEventGameEntity gamingEventGameEntity) {
        var model = new GamingEventGameModel();
        model.setGame(GameConverter.convert(gamingEventGameEntity.getGame()));
        model.setComment(gamingEventGameEntity.getComment());
        model.setGameStatus(gamingEventGameEntity.getGameStatus());
        return model;
    }

    public GamingEventParticipantsModel map(GamingEventParticipantsEntity entity) {
        var model = new GamingEventParticipantsModel();
        model.setParticipant(PlayerMapper.map(entity.getParticipant()));
        model.setParticipationStatus(entity.getParticipationStatus());
        model.setComment(entity.getComment());
        return model;
    }

    public GamingEventEntity map(GamingEventModel model) {
        var entity = new GamingEventEntity();
        entity.setDescription(model.getDescription());
        entity.setStart(ZonedDateTime.ofInstant(Instant.ofEpochMilli(model.getStart()), ZoneOffset.UTC));
        entity.setGameGroup(gameGroupService.find(model.getGameGroupId()).orElseThrow());
        entity.setSchedule(model.getSchedule());
        return entity;
    }
}
