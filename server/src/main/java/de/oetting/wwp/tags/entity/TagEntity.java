package de.oetting.wwp.tags.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "GAME_TAG")
public class TagEntity {

    @Id
    @NotNull
    @Length(max = 35)
    private String id;

    @NotNull
    @Length(max = 80)
    private String description;

    @Column(name = "SORT_ORDER")
    private int order;

    @NotNull
    private TagType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public TagType getType() {
        return type;
    }

    public void setType(TagType type) {
        this.type = type;
    }
}
