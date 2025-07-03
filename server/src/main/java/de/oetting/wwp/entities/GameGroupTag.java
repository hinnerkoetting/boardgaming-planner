package de.oetting.wwp.entities;

import de.oetting.wwp.game.entity.Game;
import de.oetting.wwp.tags.entity.TagEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "GAME_GROUP_2_TAGS")
public class GameGroupTag {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="GAME_ID", nullable=false, updatable=false)
    private Game game;

    @ManyToOne
    @JoinColumn(name="TAG_ID", nullable=false, updatable=false)
    private TagEntity tag;

    @ManyToOne
    @JoinColumn(name="GAME_GROUP_ID", nullable=false, updatable=false)
    private GameGroup gameGroup;

    public GameGroupTag() {
        // empty
    }

    public GameGroupTag(Game game, TagEntity tag, GameGroup gameGroup) {
        this.game = game;
        this.tag = tag;
        this.gameGroup = gameGroup;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public TagEntity getTag() {
        return tag;
    }

    public void setTag(TagEntity tag) {
        this.tag = tag;
    }

    public GameGroup getGameGroup() {
        return gameGroup;
    }

    public void setGameGroup(GameGroup gameGroup) {
        this.gameGroup = gameGroup;
    }
}
