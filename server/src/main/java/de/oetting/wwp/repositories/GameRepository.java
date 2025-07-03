package de.oetting.wwp.repositories;

import de.oetting.wwp.entities.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "game", path = "game")
public interface GameRepository extends PagingAndSortingRepository<Game, Long>, CrudRepository<Game,Long> {

    @Override
    List<Game> findAll();

}
