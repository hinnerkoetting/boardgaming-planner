package de.oetting.wwp.tags.controller;

import de.oetting.wwp.exceptions.ConflictException;
import de.oetting.wwp.security.Role;
import de.oetting.wwp.tags.entity.TagEntity;
import de.oetting.wwp.tags.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public Iterable<TagEntity> listAll() {
        return tagRepository.findAll();
    }

    @PostMapping
    @Transactional
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    public void create(@RequestBody TagEntity tag ) {
        if (tagRepository.findById(tag.getId()).isEmpty()) {
            tagRepository.save(tag);
        } else{
            throw new ConflictException("Tag already existss");
        }
    }

    @DeleteMapping(path = "/{id}")
    @Transactional
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    public void delete(@PathVariable("id") String id) {
        tagRepository.deleteById(id);
    }

    @PutMapping(path = "/{id}")
    @Transactional
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    public void update(@RequestBody TagEntity tag, @PathVariable("id") String id) {
        if (!id.equals(tag.getId())) {
            throw new ConflictException("Incorrect id passed");
        }
        TagEntity storedTag = tagRepository.findById(id).orElseThrow();
        storedTag.setDescription(tag.getDescription());
        storedTag.setOrder(tag.getOrder());
        storedTag.setType(tag.getType());
    }
}
