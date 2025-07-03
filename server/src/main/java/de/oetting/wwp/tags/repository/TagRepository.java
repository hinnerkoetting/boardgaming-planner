package de.oetting.wwp.tags.repository;

import de.oetting.wwp.tags.entity.TagEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends PagingAndSortingRepository<TagEntity, String>, CrudRepository<TagEntity,String> {
}
