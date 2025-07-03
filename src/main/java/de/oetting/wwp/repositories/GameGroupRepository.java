package de.oetting.wwp.repositories;

import de.oetting.wwp.entities.Game;
import de.oetting.wwp.entities.GameGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "gameGroup", path = "gameGroup")
public interface GameGroupRepository extends PagingAndSortingRepository<GameGroup, Long>, CrudRepository<GameGroup,Long> {

}
