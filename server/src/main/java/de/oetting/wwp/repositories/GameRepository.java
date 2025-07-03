package de.oetting.wwp.repositories;

import de.oetting.wwp.entities.Game;
import de.oetting.wwp.entities.GameGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "games", path = "games")
public interface GameRepository extends PagingAndSortingRepository<Game, Long>, CrudRepository<Game,Long> {

    @Override
    List<Game> findAll();

    @Override
    @RestResource(exported = false)
    void delete(Game entity);

    List<Game> findByNameContaining(String name);

}
