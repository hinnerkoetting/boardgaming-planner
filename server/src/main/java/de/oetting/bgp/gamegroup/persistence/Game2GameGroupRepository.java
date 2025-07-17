package de.oetting.bgp.gamegroup.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Game2GameGroupRepository extends PagingAndSortingRepository<Game2GameGroupRelation, Game2GameGroupId>, CrudRepository<Game2GameGroupRelation, Game2GameGroupId> {

    void deleteByGameGroupId(long gameGroupId);

    void deleteByGameId(long gameId);

}
