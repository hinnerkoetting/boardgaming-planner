package de.oetting.bgp.gamingevent.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.oetting.bgp.entities.Player;
import de.oetting.bgp.exceptions.UnprocessableEntityException;
import de.oetting.bgp.game.repository.GameRepository;
import de.oetting.bgp.gamegroup.service.GameGroupService;
import de.oetting.bgp.gamingevent.GameEventStatus;
import de.oetting.bgp.gamingevent.ParticipationStatus;
import de.oetting.bgp.gamingevent.entity.GamingEventEntity;
import de.oetting.bgp.gamingevent.entity.GamingEventGameEntity;
import de.oetting.bgp.gamingevent.entity.GamingEventGameRepository;
import de.oetting.bgp.gamingevent.entity.GamingEventParticipantsEntity;
import de.oetting.bgp.gamingevent.entity.GamingEventParticipantsRepository;
import de.oetting.bgp.gamingevent.entity.GamingEventRepository;
import de.oetting.bgp.gamingevent.entity.Schedule;
import de.oetting.bgp.gamingevent.model.AddGameToEventRequest;
import de.oetting.bgp.gamingevent.model.AddPlayerToEventRequest;
import de.oetting.bgp.gamingevent.model.GamingEventModel;
import de.oetting.bgp.gamingevent.model.GamingEventModelMapper;
import de.oetting.bgp.gamingevent.service.GamingEventService;
import de.oetting.bgp.player.persistence.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "api/")
public class GamingEventController {

    @Autowired
    private GamingEventRepository gamingEventRepository;

    @Autowired
    private GameGroupService gameGroupService;

    @Autowired
    private GamingEventGameRepository gamingEventGameRepository;

    @Autowired
    private GamingEventParticipantsRepository gamingEventParticipantsRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamingEventModelMapper gamingEventModelMapper;

    @Autowired
    private GamingEventService gamingEventService;

    @Transactional
    @GetMapping("/gameGroup/{gameGroupId}/gamingEvents")
    public List<GamingEventModel> listGamingEvents(@PathVariable("gameGroupId") long gameGroupId,
                                                   @RequestParam(name = "startTime", required = false) Long startTime,
                                                   @RequestParam(name = "number", defaultValue = "5") int number) {
        var events = findGameEvents(gameGroupId, startTime, number);

        return events.stream()
                .map(gamingEventModelMapper::map)
                .toList();
    }

    @Transactional
    @GetMapping("/gamingEvents/{gamingEventId}")
    public GamingEventModel getGamingEvent(@PathVariable("gamingEventId") long gamingEventId) {
        return gamingEventModelMapper.map(gamingEventRepository.findById(gamingEventId).orElseThrow());
    }

    @Transactional
    @PostMapping("/gameGroup/{gameGroupId}/gamingEvents")
    @ResponseStatus(value = HttpStatus.CREATED)
    public GamingEventModel createGamingEvent(@PathVariable("gameGroupId") long gameGroupId, @RequestBody GamingEventModel model) {
        gameGroupService.checkUserIsPartOfGroup(gameGroupId);
        if (gameGroupId != model.getGameGroupId()) {
            throw new UnprocessableEntityException("GameGroupId must have same value in url and body");
        }

        var entity = gamingEventModelMapper.map(model);
        GamingEventEntity savedEntity = gamingEventRepository.save(entity);
        return gamingEventModelMapper.map(savedEntity);
    }

    @Transactional
    @DeleteMapping("/gamingEvents/{gamingEventId}")
    public void deleteGamingEvent(@PathVariable("gamingEventId") long gamingEventId) {
        var gamingEvent = gamingEventRepository.findById(gamingEventId);
        var gameGroupId = gamingEvent.orElseThrow().getGameGroup().getId();
        gameGroupService.checkUserIsPartOfGroup(gameGroupId);

        gamingEventParticipantsRepository.deleteByGamingEventId(gamingEventId);
        gamingEventGameRepository.deleteByGamingEventId(gamingEventId);
        gamingEventRepository.deleteById(gamingEventId);
    }

    @Transactional
    @PostMapping("/gameGroup/{gameGroupId}/gamingEvents/{gamingEventId}/clone")
    @ResponseStatus(value = HttpStatus.CREATED)
    public GamingEventModel cloneEvent(@PathVariable("gameGroupId") long gameGroupId, @PathVariable("gamingEventId") long gamingEventId) {
        gameGroupService.checkUserIsPartOfGroup(gameGroupId);
        var toBeCloned = gamingEventRepository.findById(gamingEventId).orElseThrow();

        GamingEventEntity savedEntity = gamingEventService.cloneEvent(toBeCloned);

        return gamingEventModelMapper.map(savedEntity);
    }


    @Transactional
    @PutMapping("/gameGroup/{gameGroupId}/gamingEvents/{gamingEventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public GamingEventModel updateGamingEvent(@PathVariable("gameGroupId") long gameGroupId,
                                              @PathVariable("gamingEventId") long gamingEventId,
                                              @RequestBody GamingEventModel model) {
        gameGroupService.checkUserIsPartOfGroup(gameGroupId);
        if (gamingEventId != model.getId()) {
            throw new UnprocessableEntityException("gamingEventId must have same value in url and body");
        }

        GamingEventEntity gamingEvent = gamingEventRepository.findById(model.getId()).orElseThrow();
        if (model.getGameGroupId() != gamingEvent.getGameGroup().getId()) {
            throw new UnprocessableEntityException("GameGroupId cannot be changed");
        }
        update(model, gamingEvent);
        return gamingEventModelMapper.map(gamingEvent);
    }

    @Transactional
    @PostMapping("/gamingEvents/{gamingEventId}/player")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addPlayer(@PathVariable("gamingEventId") long gamingEventId, @RequestBody AddPlayerToEventRequest request) {
        GamingEventEntity gamingEvent = gamingEventRepository.findById(gamingEventId).orElseThrow();
        var player = playerRepository.findById(request.getPlayerId()).orElseThrow();
        gameGroupService.checkUserIsPartOfGroup(gamingEvent.getGameGroup().getId());

        var participant = new GamingEventParticipantsEntity();
        participant.setGamingEvent(gamingEvent);
        participant.setParticipationStatus(request.getParticipationStatus());
        participant.setParticipant(player);
        gamingEventParticipantsRepository.save(participant);
    }

    @Transactional
    @DeleteMapping("/gamingEvents/{gamingEventId}/player/{playerId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deletePlayer(@PathVariable("gamingEventId") long gamingEventId, @PathVariable("playerId") long playerId) {
        GamingEventEntity gamingEvent = gamingEventRepository.findById(gamingEventId).orElseThrow();

        gameGroupService.checkUserIsPartOfGroup(gamingEvent.getGameGroup().getId());
        gamingEventParticipantsRepository.deleteByGamingEventIdAndParticipantId(gamingEventId, playerId);
    }

    @Transactional
    @PostMapping("/gamingEvents/{gamingEventId}/game")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addGame(@PathVariable("gamingEventId") long gamingEventId, @RequestBody AddGameToEventRequest request) {
        GamingEventEntity gamingEvent = gamingEventRepository.findById(gamingEventId).orElseThrow();
        var game = gameRepository.findById(request.getGameId()).orElseThrow();
        gameGroupService.checkUserIsPartOfGroup(gamingEvent.getGameGroup().getId());

        var gamingEventGame = new GamingEventGameEntity();
        gamingEventGame.setGamingEvent(gamingEvent);
        gamingEventGame.setGameStatus(request.getGameStatus());
        gamingEventGame.setGame(game);
        gamingEventGame.setComment(request.getComment());
        gamingEventGameRepository.save(gamingEventGame);
    }

    @Transactional
    @DeleteMapping("/gamingEvents/{gamingEventId}/game/{gameId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void removeGameFromEvent(@PathVariable("gamingEventId") long gamingEventId, @PathVariable("gameId") long gameId) {
        GamingEventEntity gamingEvent = gamingEventRepository.findById(gamingEventId).orElseThrow();

        gameGroupService.checkUserIsPartOfGroup(gamingEvent.getGameGroup().getId());
        gamingEventGameRepository.deleteByGamingEventIdAndGameId(gamingEventId, gameId);
    }

    @Transactional
    @PutMapping("/gamingEvents/{gamingEventId}/game/{gameId}/status")
    public void updateGameStatus(@PathVariable("gamingEventId") long gamingEventId, @PathVariable("gameId") long gameId, @RequestBody JsonNode body) {
        var status = body.findValue("status").asText();
        var game = gamingEventGameRepository.findByGamingEventIdAndGameId(gamingEventId, gameId).orElseThrow();

        game.setGameStatus(GameEventStatus.valueOf(status));
    }

    @Transactional
    @PostMapping("/gamingEvents/{gamingEventId}/player/all")
    @ResponseStatus(value = HttpStatus.CREATED)
    public GamingEventModel addAllGroupPlayersToEvent(@PathVariable("gamingEventId") long gamingEventId) {
        GamingEventEntity gamingEvent = gamingEventRepository.findById(gamingEventId).orElseThrow();
        gameGroupService.checkUserIsPartOfGroup(gamingEvent.getGameGroup().getId());

        var allGroupPlayers = gamingEvent.getGameGroup().getPlayers();
        allGroupPlayers.forEach(player -> {
            addPlayerIfMissing(player.getPlayer(), gamingEvent);
        });
        return gamingEventModelMapper.map(gamingEvent);
    }

    @Transactional
    @PutMapping("/gamingEvents/{gamingEventId}/player/{playerId}/status")
    public void updateParticipationStatus(@PathVariable("gamingEventId") long gamingEventId, @PathVariable("playerId") long playerId, @RequestBody JsonNode body) {
        var status = body.findValue("status").asText();
        var participant = gamingEventParticipantsRepository.findByGamingEventIdAndParticipantId(gamingEventId, playerId).orElseThrow();
        gameGroupService.checkUserIsPartOfGroup(participant.getGamingEvent().getGameGroup().getId());

        participant.setParticipationStatus(ParticipationStatus.valueOf(status));
    }
    
    private void addPlayerIfMissing(Player player, GamingEventEntity gamingEvent) {
        boolean doesNotExistYet = gamingEvent.getParticipants().stream().noneMatch(participant -> Objects.equals(participant.getParticipant().getId(), player.getId()));

        if (doesNotExistYet) {
            var newParticipant = new GamingEventParticipantsEntity();
            newParticipant.setGamingEvent(gamingEvent);
            newParticipant.setParticipationStatus(ParticipationStatus.NOT_RESPONDED);
            newParticipant.setParticipant(player);
            gamingEventParticipantsRepository.save(newParticipant);
            gamingEvent.getParticipants().add(newParticipant);
        }
    }

    private List<GamingEventEntity> findGameEvents(long gameGroupId,
                                                   Long startTime,
                                                   int number) {
        var startInstant = startTime != null ? Instant.ofEpochMilli(startTime) : Instant.now();
        var zonedDateTime = ZonedDateTime.ofInstant(startInstant, ZoneOffset.UTC);

        // It's a bit difficult to exactly return the number of requested events if we always want to include recurring events.
        // I think it's good enough for now to just return more, but this needs to be fixed at some point.
        var recurringEvents = gamingEventRepository.findByGameGroupIdAndScheduleIn(gameGroupId, Arrays.asList(Schedule.MONTHLY, Schedule.WEEKLY));
        var singleEvents = gamingEventRepository.findByGameGroupIdAndScheduleAndStartAfter(gameGroupId, Schedule.ONCE, zonedDateTime, PageRequest.of(0, number, Sort.by("start")));

        var result = new ArrayList<>(singleEvents);
        result.addAll(recurringEvents);
        return result;
    }

    private void update(GamingEventModel model, GamingEventEntity entity) {
        entity.setStart(ZonedDateTime.ofInstant(Instant.ofEpochMilli(model.getStart()), ZoneOffset.UTC));
        entity.setDescription(model.getDescription());
        entity.setSchedule(model.getSchedule());
        entity.setParent(model.getParentEventId() != null ? gamingEventRepository.findById(model.getParentEventId()).orElseThrow() : null);
    }


}
