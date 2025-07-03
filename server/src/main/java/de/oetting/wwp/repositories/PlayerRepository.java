package de.oetting.wwp.repositories;

import de.oetting.wwp.entities.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends PagingAndSortingRepository<Player, Long>, CrudRepository<Player,Long> {

    Optional<Player> findByName(String name);

}
