package de.oetting.bgp.gamingevent.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GamingEventGameRepository extends CrudRepository<GamingEventGameEntity,Long> {


    void deleteByGamingEventIdAndGameId(long gamingEventId, long gameId);

    Optional<GamingEventGameEntity> findByGamingEventIdAndGameId(long gamingEventId, long gameId);

}
