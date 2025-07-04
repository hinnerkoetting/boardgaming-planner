package de.oetting.bgp.gamingevent.entity;

import org.springframework.data.repository.CrudRepository;

public interface GamingEventParticipantsRepository extends CrudRepository<GamingEventParticipantsEntity,Long> {


    void deleteByGamingEventIdAndParticipantId(long gamingEventId, long participantId);
}
