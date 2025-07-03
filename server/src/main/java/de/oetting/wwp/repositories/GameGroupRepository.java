package de.oetting.wwp.repositories;

import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.security.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "gameGroups", path = "gameGroups")
public interface GameGroupRepository extends PagingAndSortingRepository<GameGroup, Long>, CrudRepository<GameGroup,Long> {

    @Query("select p from Player p " +
            "join p.gameGroups gg " +
            "where gg.id = :gameGroupId and p.id = :playerId")
    Optional<Player> playerAssignedToGameGroup(long playerId, long gameGroupId);

    @Override
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    void delete(GameGroup entity);

}
