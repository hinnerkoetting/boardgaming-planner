package de.oetting.wwp.tags.repository;

import de.oetting.wwp.tags.entity.TagEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends PagingAndSortingRepository<TagEntity, Long>, CrudRepository<TagEntity,Long> {

    @Query(value = "SELECT MAX(t.ranking) FROM TagEntity t")
    Integer findMaxRanking();

}
