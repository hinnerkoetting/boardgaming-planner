package de.oetting.bgp.gamingevent.service;

import de.oetting.bgp.gamingevent.entity.GamingEventEntity;
import de.oetting.bgp.gamingevent.entity.GamingEventGameEntity;
import de.oetting.bgp.gamingevent.entity.GamingEventGameRepository;
import de.oetting.bgp.gamingevent.entity.GamingEventParticipantsEntity;
import de.oetting.bgp.gamingevent.entity.GamingEventParticipantsRepository;
import de.oetting.bgp.gamingevent.entity.GamingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamingEventService {

    @Autowired
    private GamingEventRepository gamingEventRepository;

    @Autowired
    private GamingEventParticipantsRepository gamingEventParticipantsRepository;

    @Autowired
    private GamingEventGameRepository gamingEventGameRepository;

    public GamingEventEntity cloneEvent(GamingEventEntity toBeCloned) {

        var newEntity = new GamingEventEntity();
        newEntity.setSchedule(toBeCloned.getSchedule());
        newEntity.setStart(toBeCloned.getStart());
        newEntity.setGameGroup(toBeCloned.getGameGroup());
        GamingEventEntity savedEntity = gamingEventRepository.save(newEntity);
        savedEntity.setGames(clone(toBeCloned, savedEntity));
        savedEntity.setParticipants(cloneParticipants(toBeCloned, savedEntity));

        return savedEntity;
    }

    private List<GamingEventGameEntity> clone(GamingEventEntity toBeCloned, GamingEventEntity newEntity) {
        return toBeCloned.getGames() != null ? toBeCloned.getGames().stream().map(g -> gamingEventGameRepository.save(g.cloneTo(newEntity))).toList() : null;
    }

    private List<GamingEventParticipantsEntity> cloneParticipants(GamingEventEntity toBeCloned, GamingEventEntity newEntity) {
        return toBeCloned.getParticipants() != null ? toBeCloned.getParticipants().stream().map(p -> gamingEventParticipantsRepository.save(p.cloneTo(newEntity))).toList() : null;
    }

}
