package de.oetting.wwp.repositories;

import de.oetting.wwp.entities.Interest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "interests", path = "interests")
public interface InterestRepository  extends PagingAndSortingRepository<Interest, Long>,CrudRepository<Interest,Long> {
}
