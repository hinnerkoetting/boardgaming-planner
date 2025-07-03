package de.oetting.wwp.repositories;

import de.oetting.wwp.entities.Interest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository  extends PagingAndSortingRepository<Interest, Long>,CrudRepository<Interest,Long> {

    List<Interest> findByGameGroupIdAndPlayerId(long gameGroupId, long playerId);

    Optional<Interest> findByGameGroupIdAndPlayerIdAndGameId(long gameGroupId, long playerId, long gameId);

    void deleteByGameGroupIdAndPlayerIdAndGameId(long gameGroupId, long playerId, long gameId);

}
