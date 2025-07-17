package de.oetting.bgp.game.repository;

import de.oetting.bgp.game.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends PagingAndSortingRepository<Game, Long>, CrudRepository<Game, Long> {

    List<Game> findByNameContainingIgnoreCase(String name);

}
