package de.oetting.bgp.gamingevent.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GamingEventParticipantsRepository extends CrudRepository<GamingEventParticipantsEntity,Long> {

    void deleteByGamingEventIdAndParticipantId(long gamingEventId, long participantId);
    void deleteByGamingEventId(long gamingEventId);

    Optional<GamingEventParticipantsEntity> findByGamingEventIdAndParticipantId(long gamingEventId, long participantId);
}
