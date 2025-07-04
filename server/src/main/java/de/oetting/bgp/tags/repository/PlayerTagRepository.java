package de.oetting.bgp.tags.repository;

import de.oetting.bgp.tags.entity.PlayerTagEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerTagRepository extends CrudRepository<PlayerTagEntity, Long> {

    List<PlayerTagEntity> findByGameGroupId(long gameGroupId);

    Optional<PlayerTagEntity> findByGameGroupIdAndGameIdAndTagIdAndPlayerId(long gameGroupId, long gameId, long tagId, long playerId);
}
