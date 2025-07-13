package de.oetting.bgp.gamingevent.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GamingEventGameRepository extends CrudRepository<GamingEventGameEntity,Long> {


    void deleteByGamingEventIdAndGameId(long gamingEventId, long gameId);

    void deleteByGamingEventId(long gamingEventId);

    Optional<GamingEventGameEntity> findByGamingEventIdAndGameId(long gamingEventId, long gameId);

    @Query("from GamingEventGameEntity" +
            " where game.id = :gameId" +
            " order by gamingEvent.start desc")
    Optional<GamingEventGameEntity> findByGameIdAndLastByOrderByGamingEventStart(long gameId);

    Integer countByGameId(long GameId);

}
