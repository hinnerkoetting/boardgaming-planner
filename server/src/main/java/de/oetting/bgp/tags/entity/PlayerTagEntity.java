package de.oetting.bgp.tags.entity;

import de.oetting.bgp.entities.Player;
import de.oetting.bgp.game.entity.Game;
import de.oetting.bgp.gamegroup.persistence.GameGroupEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "PLAYER_2_TAGS")
public class PlayerTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GAME_ID", nullable = false, updatable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "TAG_ID", nullable = false, updatable = false)
    private TagEntity tag;

    @ManyToOne
    @JoinColumn(name = "GAME_GROUP_ID", nullable = false, updatable = false)
    private GameGroupEntity gameGroup;

    @ManyToOne
    @JoinColumn(name = "PLAYER_ID", nullable = false, updatable = false)
    private Player player;

    public PlayerTagEntity() {
        // empty
    }

    public PlayerTagEntity(Game game, TagEntity tag, GameGroupEntity gameGroup, Player player) {
        this.game = game;
        this.tag = tag;
        this.gameGroup = gameGroup;
        this.player = player;
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

    public GameGroupEntity getGameGroup() {
        return gameGroup;
    }

    public void setGameGroup(GameGroupEntity gameGroup) {
        this.gameGroup = gameGroup;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
