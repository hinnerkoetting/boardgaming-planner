package de.oetting.wwp.repositories;

import de.oetting.wwp.entities.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "player", path = "player")
public interface PlayerRepository extends PagingAndSortingRepository<Player, Long>, CrudRepository<Player,Long> {

    @Override
    List<Player> findAll();

    Player findByName(String name);
}
