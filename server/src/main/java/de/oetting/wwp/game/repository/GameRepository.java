package de.oetting.wwp.game.repository;

import de.oetting.wwp.entities.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends PagingAndSortingRepository<Game, Long>, CrudRepository<Game,Long> {

    List<Game> findByNameContaining(String name);

}
