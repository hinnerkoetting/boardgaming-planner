package de.oetting.bgp.gamingevent;

import de.oetting.bgp.gamingevent.entity.GamingEventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface GamingEventRepository extends CrudRepository<GamingEventEntity,Long> {

    List<GamingEventEntity> findByGameGroupId(long gameGroupId, Pageable pageable);

    List<GamingEventEntity> findByGameGroupIdAndStartAfter(long gameGroupId, ZonedDateTime start, Pageable pageable);

}
