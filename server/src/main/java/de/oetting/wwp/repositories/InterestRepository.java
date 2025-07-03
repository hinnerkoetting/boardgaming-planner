package de.oetting.wwp.repositories;

import de.oetting.wwp.entities.Interest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository  extends PagingAndSortingRepository<Interest, Long>,CrudRepository<Interest,Long> {

    List<Interest> findByGameGroupIdAndPlayerId(long gameGroupId, long playerId);
}
