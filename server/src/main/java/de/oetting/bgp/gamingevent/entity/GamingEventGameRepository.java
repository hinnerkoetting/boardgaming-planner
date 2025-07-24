package de.oetting.bgp.gamingevent.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GamingEventGameRepository extends CrudRepository<GamingEventGameEntity, Long> {

    void deleteByGamingEventIdAndGameId(long gamingEventId, long gameId);

    void deleteByGamingEventId(long gamingEventId);

    Optional<GamingEventGameEntity> findByGamingEventIdAndGameId(long gamingEventId, long gameId);

    @Query("select count(e) from GamingEventGameEntity e" +
            " where game.id = :gameId" +
            " and exists (select p from GamingEventParticipantsEntity p where p.gamingEvent = e.gamingEvent and p.participant.id = :playerId and p.participationStatus in ('CONFIRMED', 'MAYBE'))")
    Integer countByGameId(long gameId, long playerId);

}
