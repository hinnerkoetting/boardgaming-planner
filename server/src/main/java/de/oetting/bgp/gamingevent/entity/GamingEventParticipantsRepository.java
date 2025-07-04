package de.oetting.bgp.gamingevent.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GamingEventParticipantsRepository extends CrudRepository<GamingEventParticipantsEntity,Long> {

    void deleteByGamingEventIdAndParticipantId(long gamingEventId, long participantId);

    Optional<GamingEventParticipantsEntity> findByGamingEventIdAndParticipantId(long gamingEventId, long participantId);
}
