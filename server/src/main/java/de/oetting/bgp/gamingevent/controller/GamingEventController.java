package de.oetting.bgp.gamingevent.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.oetting.bgp.entities.Player;
import de.oetting.bgp.exceptions.UnprocessableEntityException;
import de.oetting.bgp.game.repository.GameRepository;
import de.oetting.bgp.gamegroup.service.GameGroupService;
import de.oetting.bgp.gamingevent.GameEventStatus;
import de.oetting.bgp.gamingevent.ParticipationStatus;
import de.oetting.bgp.gamingevent.entity.*;
import de.oetting.bgp.gamingevent.model.*;
import de.oetting.bgp.player.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.*;

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

    @Transactional
    @GetMapping("/gameGroup/{gameGroupId}/gamingEvents")
    public List<GamingEventModel> listGamingEvents(@PathVariable("gameGroupId") long gameGroupId,
                                                   @RequestParam(name = "onlyNext", defaultValue = "false") boolean onlyNext,
                                                   @RequestParam(name = "number", defaultValue = "5") int number) {
        var events = findGameEvents(gameGroupId, onlyNext, number);

        return events.stream()
                .map(this::map)
                .toList();
    }

    @Transactional
    @GetMapping("/gamingEvents/{gamingEventId}")
    public GamingEventModel getGamingEvent(@PathVariable("gamingEventId") long gamingEventId) {
       return map(gamingEventRepository.findById(gamingEventId).orElseThrow());
    }

    @Transactional
    @PostMapping("/gameGroup/{gameGroupId}/gamingEvents")
    @ResponseStatus(value = HttpStatus.CREATED)
    public GamingEventModel createGamingEvent(@PathVariable("gameGroupId") long gameGroupId, @RequestBody GamingEventModel model) {
        gameGroupService.checkUserIsPartOfGroup(gameGroupId);
        if (gameGroupId != model.getGameGroupId()) {
            throw new UnprocessableEntityException("GameGroupId must have same value in url and body");
        }

        var entity = map(model);
        GamingEventEntity savedEntity = gamingEventRepository.save(entity);
        return map(savedEntity);
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

        var newEntity = new GamingEventEntity();
        newEntity.setSchedule(toBeCloned.getSchedule());
        newEntity.setGames(toBeCloned.getGames() != null ? new ArrayList<>(toBeCloned.getGames()) : null);
        newEntity.setStart(toBeCloned.getStart());
        newEntity.setGameGroup(toBeCloned.getGameGroup());
        newEntity.setParticipants(toBeCloned.getParticipants() != null ? new ArrayList<>(toBeCloned.getParticipants()) : null);
        GamingEventEntity savedEntity = gamingEventRepository.save(newEntity);
        return map(savedEntity);
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
        return map(gamingEvent);
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
            addPlayerIfMissing(player, gamingEvent);
        });
        return map(gamingEvent);
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
                                                   boolean onlyNext,
                                                   int number) {
        if (onlyNext) {
            return gamingEventRepository.findByGameGroupIdAndStartAfter(gameGroupId, ZonedDateTime.now(), PageRequest.of(0, number, Sort.by("start")));
        }
        return gamingEventRepository.findByGameGroupId(gameGroupId, PageRequest.of(0, number, Sort.by("start")));
    }

    private static void update(GamingEventModel model, GamingEventEntity gamingEvent) {
        gamingEvent.setStart(model.getStart());
        gamingEvent.setDescription(model.getDescription());
        gamingEvent.setSchedule(model.getSchedule());
    }

    private GamingEventModel map(GamingEventEntity entity) {
        var model = new GamingEventModel();
        model.setDescription(entity.getDescription());
        model.setStart(entity.getStart());
        model.setId(entity.getId());
        model.setGames(entity.getGames() == null ? Collections.emptyList() : entity.getGames().stream().map(this::map).toList());
        model.setParticipants(entity.getParticipants() == null ? Collections.emptyList() : entity.getParticipants().stream().map(this::map).toList());
        model.setSchedule(entity.getSchedule());
        return model;
    }

    private GamingEventGameModel map(GamingEventGameEntity gamingEventGameEntity) {
        var model = new GamingEventGameModel();
        model.setGame(gamingEventGameEntity.getGame());
        model.setComment(gamingEventGameEntity.getComment());
        model.setGameStatus(gamingEventGameEntity.getGameStatus());
        return model;
    }

    private GamingEventParticipantsModel map(GamingEventParticipantsEntity entity) {
        var model = new GamingEventParticipantsModel();
        model.setParticipant(entity.getParticipant());
        model.setParticipationStatus(entity.getParticipationStatus());
        model.setComment(entity.getComment());
        return model;
    }

    private GamingEventEntity map(GamingEventModel model) {
        var entity = new GamingEventEntity();
        entity.setDescription(model.getDescription());
        entity.setStart(model.getStart());
        entity.setGameGroup(gameGroupService.find(model.getGameGroupId()).orElseThrow());
        entity.setSchedule(model.getSchedule());
        return entity;
    }

}
