package de.oetting.bgp.gamegroup.persistence;

import de.oetting.bgp.game.entity.Game;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "GAME_GROUP_GAMES")
@IdClass(Game2GameGroupId.class)
public class Game2GameGroupRelation {

    @Id
    @Column(name = "GAMES_ID")
    private long gameId;

    @Id
    @Column(name = "GAME_GROUPS_ID")
    private long gameGroupId;

    @Column(name = "ADDED_DT")
    private LocalDateTime addedToGroupTime;

    @ManyToOne
    @JoinColumn(name = "GAMES_ID", insertable = false, updatable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "GAME_GROUPS_ID", insertable = false, updatable = false)
    private GameGroupEntity gameGroup;

    public Game2GameGroupRelation() {
    }

    public Game2GameGroupRelation(long gameId, long gameGroupId) {
        this.gameId = gameId;
        this.gameGroupId = gameGroupId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameGroupEntity getGameGroup() {
        return gameGroup;
    }

    public void setGameGroup(GameGroupEntity gameGroup) {
        this.gameGroup = gameGroup;
    }

    public LocalDateTime getAddedToGroupTime() {
        return addedToGroupTime;
    }

    public void setAddedToGroupTime(LocalDateTime addedToGroupTime) {
        this.addedToGroupTime = addedToGroupTime;
    }
}
