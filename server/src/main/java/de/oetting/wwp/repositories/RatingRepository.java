package de.oetting.wwp.repositories;

import de.oetting.wwp.entities.Rating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends PagingAndSortingRepository<Rating, Long>,CrudRepository<Rating,Long> {

    List<Rating> findByGameGroupIdAndPlayerId(long gameGroupId, long playerId);
    List<Rating> findByGameGroupId(long gameGroupId);
    List<Rating> findByGameGroupIdAndGameId(long gameGroupId, long gameId);

    Optional<Rating> findByGameGroupIdAndPlayerIdAndGameId(long gameGroupId, long playerId, long gameId);

    void deleteByGameGroupIdAndPlayerIdAndGameId(long gameGroupId, long playerId, long gameId);

    void deleteByGameId(long gameId);

    void deleteByPlayerId(long playerId);
}
