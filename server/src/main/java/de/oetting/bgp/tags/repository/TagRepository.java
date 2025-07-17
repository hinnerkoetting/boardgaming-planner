package de.oetting.bgp.tags.repository;

import de.oetting.bgp.tags.entity.TagEntity;
import de.oetting.bgp.tags.entity.TagType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends PagingAndSortingRepository<TagEntity, Long>, CrudRepository<TagEntity, Long> {

    @Query(value = "SELECT MAX(t.ranking) FROM TagEntity t")
    Integer findMaxRanking();

    List<TagEntity> findByType(TagType tagType);
}
