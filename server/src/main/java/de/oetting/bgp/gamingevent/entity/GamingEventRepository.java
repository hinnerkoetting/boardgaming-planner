package de.oetting.bgp.gamingevent.entity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface GamingEventRepository extends CrudRepository<GamingEventEntity,Long> {

    List<GamingEventEntity> findByGameGroupId(long gameGroupId, Pageable pageable);

    List<GamingEventEntity> findByGameGroupIdAndScheduleAndStartAfter(long gameGroupId, Schedule schedule, ZonedDateTime start, Pageable pageable);

    List<GamingEventEntity> findByGameGroupIdAndScheduleIn(long gameGroupId, List<Schedule> schedules);

}
