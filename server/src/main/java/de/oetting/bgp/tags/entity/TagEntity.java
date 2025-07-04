package de.oetting.bgp.tags.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "GAME_TAG")
public class TagEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    @Length(max = 80)
    private String description;

    @Column(name = "RANKING")
    private int ranking;

    @NotNull
    private TagType type;

    /** Used to create a mapping so that we can automatically add tags to imported games */
    @Column@Length(max = 200)
    private String importedSourceName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public TagType getType() {
        return type;
    }

    public void setType(TagType type) {
        this.type = type;
    }

    public String getImportedSourceName() {
        return importedSourceName;
    }

    public void setImportedSourceName(String importedSourceName) {
        this.importedSourceName = importedSourceName;
    }
}
