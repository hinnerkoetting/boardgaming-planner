package de.oetting.bgp.gamingevent.entity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface GamingEventRepository extends CrudRepository<GamingEventEntity,Long> {

    List<GamingEventEntity> findByGameGroupId(long gameGroupId, Pageable pageable);

    List<GamingEventEntity> findByGameGroupIdAndScheduleAndStartAfter(long gameGroupId, Schedule schedule, ZonedDateTime start, Pageable pageable);

    List<GamingEventEntity> findByGameGroupIdAndScheduleIn(long gameGroupId, List<Schedule> schedules);

    @Query("from GamingEventEntity ge" +
            " where element(ge.games).game.id = :gameId" +
            " and exists (select p from GamingEventParticipantsEntity p where p.gamingEvent = gamingEvent and p.participant.id = :playerId and p.participationStatus in ('CONFIRMED', 'MAYBE'))")
    List<GamingEventEntity> findGamingEventsWithGame(long gameId, long playerId);

    @Query("from GamingEventEntity ge" +
            " where ge.gameGroup.id = :gameGroupId" +
            " and exists (select p from GamingEventParticipantsEntity p where p.gamingEvent = gamingEvent and p.participant.id = :playerId and p.participationStatus in ('CONFIRMED', 'MAYBE'))")
    List<GamingEventEntity> findGamingEvents(long gameGroupId, long playerId);
}
