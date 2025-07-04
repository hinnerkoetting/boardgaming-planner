package de.oetting.bgp.gamingevent.entity;

import org.springframework.data.repository.CrudRepository;

public interface GamingEventGameRepository extends CrudRepository<GamingEventGameEntity,Long> {


    void deleteByGamingEventIdAndGameId(long gamingEventId, long gameId);

}
