package de.oetting.wwp.tags.repository;

import de.oetting.wwp.entities.GameGroupTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameGroupTagRepository extends CrudRepository<GameGroupTag, Long> {

    List<GameGroupTag> findByGameGroupId(long gameGroupId);

    Optional<GameGroupTag> findByGameGroupIdAndGameIdAndTagId(long gameGroupId, long gameId, long tagId);
}
