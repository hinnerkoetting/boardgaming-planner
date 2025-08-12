package de.oetting.bgp.gamegroup.persistence;

import de.oetting.bgp.entities.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface GameGroupRepository extends PagingAndSortingRepository<GameGroupEntity, Long>, CrudRepository<GameGroupEntity, Long> {

    @Query("select p from Player p " +
            "join p.gameGroups gg " +
            "where gg.id = :gameGroupId and p.id = :playerId")
    Optional<Player> playerAssignedToGameGroup(long playerId, long gameGroupId);

    boolean existsByName(String name);

    List<GameGroupEntity> findByType(GameGroupType type);

}
