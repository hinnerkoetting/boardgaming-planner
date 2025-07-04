package de.oetting.wwp.tags.repository;

import de.oetting.wwp.tags.entity.GameGroupTagEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameGroupTagRepository extends CrudRepository<GameGroupTagEntity, Long> {

    List<GameGroupTagEntity> findByGameGroupId(long gameGroupId);

    Optional<GameGroupTagEntity> findByGameGroupIdAndGameIdAndTagId(long gameGroupId, long gameId, long tagId);
}
