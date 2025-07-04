package de.oetting.bgp.gamingevent.controller;

import de.oetting.bgp.exceptions.UnprocessableEntityException;
import de.oetting.bgp.gamegroup.service.GameGroupService;
import de.oetting.bgp.gamingevent.GamingEventRepository;
import de.oetting.bgp.gamingevent.entity.GamingEventEntity;
import de.oetting.bgp.gamingevent.model.GamingEventModel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/gamingEvents")
public class GamingEventController {


    @Autowired
    private GamingEventRepository gamingEventRepository;

    @Autowired
    private GameGroupService gameGroupService;

    @Transactional
    @GetMapping("/{gameGroupId}")
    public List<GamingEventModel> listGamingEvents(@PathVariable("gameGroupId") long gameGroupId,
                                                   @RequestParam(name = "onlyNext", defaultValue = "false") boolean onlyNext,
                                                   @RequestParam(name = "number", defaultValue = "5") int number){
        var events = findGameEvents(gameGroupId, onlyNext, number);

        return events.stream().map(this::map).toList();
    }



    @Transactional
    @PostMapping("/{gameGroupId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public GamingEventModel createGamingEvent(@PathVariable("gameGroupId") long gameGroupId, @RequestBody GamingEventModel model) {
        gameGroupService.checkUserIsPartOfGroup(gameGroupId);
        if (gameGroupId != model.getGameGroupId()) {
            throw new UnprocessableEntityException("GameGroupId must have same value in url and body");
        }
        if (model.getEnd().isBefore(model.getStart())) {
            throw new UnprocessableEntityException("Start must be before end");
        }

        var entity = map(model);
        GamingEventEntity savedEntity = gamingEventRepository.save(entity);
        return map(savedEntity);
    }

    @Transactional
    @PutMapping("/{gameGroupId}/{gamingEventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public GamingEventModel updateGamingEvent(@PathVariable("gameGroupId") long gameGroupId,
                                              @PathVariable("gamingEventId") long gamingEventId,
                                              @RequestBody GamingEventModel model) {
        gameGroupService.checkUserIsPartOfGroup(gameGroupId);
        if (gamingEventId != model.getId()) {
            throw new UnprocessableEntityException("GameGroupId must have same value in url and body");
        }
        if (model.getEnd().isBefore(model.getStart())) {
            throw new UnprocessableEntityException("Start must be before end");
        }
        GamingEventEntity gamingEvent = gamingEventRepository.findById(model.getId()).orElseThrow();
        update(model, gamingEvent);
        return map(gamingEvent);
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
        gamingEvent.setEnd(model.getEnd());
        gamingEvent.setStart(model.getStart());
        gamingEvent.setDescription(model.getDescription());
    }

    private GamingEventModel map(GamingEventEntity gamingEventEntity) {
        var model = new GamingEventModel();
        model.setDescription(gamingEventEntity.getDescription());
        model.setEnd(gamingEventEntity.getEnd());
        model.setStart(gamingEventEntity.getStart());
        model.setId(gamingEventEntity.getId());
        return model;
    }

    private GamingEventEntity map(GamingEventModel model) {
        var entity = new GamingEventEntity();
        entity.setDescription(model.getDescription());
        entity.setEnd(model.getEnd());
        entity.setStart(model.getStart());
        entity.setGameGroup(gameGroupService.find(model.getGameGroupId()).orElseThrow());
        return entity;
    }

}
