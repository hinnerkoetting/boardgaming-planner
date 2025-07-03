package de.oetting.wwp.tags.controller;

import de.oetting.wwp.exceptions.ConflictException;
import de.oetting.wwp.security.Role;
import de.oetting.wwp.tags.entity.TagEntity;
import de.oetting.wwp.tags.model.CreateTagRequest;
import de.oetting.wwp.tags.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "api/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public Iterable<TagEntity> listAll() {
        return StreamSupport.stream(tagRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(TagEntity::getRanking))
                .toList();
    }

    @PostMapping
    @Transactional
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    public TagEntity create(@RequestBody CreateTagRequest tag) {
        TagEntity entity= new TagEntity();
        entity.setDescription(tag.getDescription());
        entity.setType(tag.getType());
        entity.setImportedSourceName(tag.getImportedSourceName());
        if (tag.getRanking() == null) {
            Integer maxRanking = tagRepository.findMaxRanking();
            entity.setRanking(maxRanking == null ? 0 : maxRanking + 1);
        }
        return tagRepository.save(entity);
    }

    @DeleteMapping(path = "/{id}")
    @Transactional
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    public void delete(@PathVariable("id") long id) {
        tagRepository.deleteById(id);
    }

    @PutMapping(path = "/{id}")
    @Transactional
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    public void update(@RequestBody CreateTagRequest tag, @PathVariable("id") Long id) {
        TagEntity storedTag = tagRepository.findById(id).orElseThrow();
        storedTag.setDescription(tag.getDescription());
        storedTag.setRanking(tag.getRanking());
        storedTag.setType(tag.getType());
        storedTag.setImportedSourceName(tag.getImportedSourceName());
    }
}
