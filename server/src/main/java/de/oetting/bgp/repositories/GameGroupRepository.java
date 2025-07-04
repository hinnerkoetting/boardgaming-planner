package de.oetting.bgp.repositories;

import de.oetting.bgp.entities.GameGroup;
import de.oetting.bgp.entities.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface GameGroupRepository extends PagingAndSortingRepository<GameGroup, Long>, CrudRepository<GameGroup,Long> {

    @Query("select p from Player p " +
            "join p.gameGroups gg " +
            "where gg.id = :gameGroupId and p.id = :playerId")
    Optional<Player> playerAssignedToGameGroup(long playerId, long gameGroupId);

    boolean existsByName(String name);
}
