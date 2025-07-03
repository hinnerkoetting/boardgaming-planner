package de.oetting.wwp.tags.model;

import de.oetting.wwp.tags.entity.TagType;

public class CreateTagRequest {

    private String description;
    private Integer ranking;
    private TagType type;
    private String importedSourceName;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
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
