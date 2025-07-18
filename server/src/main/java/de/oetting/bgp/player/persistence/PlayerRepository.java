package de.oetting.bgp.player.persistence;

import de.oetting.bgp.entities.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PlayerRepository extends PagingAndSortingRepository<Player, Long>, CrudRepository<Player, Long> {

    Optional<Player> findByName(String name);
}
