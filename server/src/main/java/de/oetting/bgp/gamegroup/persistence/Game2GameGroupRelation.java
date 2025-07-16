package de.oetting.bgp.gamegroup.persistence;

import de.oetting.bgp.game.entity.Game;
import jakarta.persistence.*;

@Entity
@Table(name = "GAME_GROUP_GAMES")
@IdClass(Game2GameGroupId.class)
public class Game2GameGroupRelation {

    @Id
    @Column(name= "GAMES_ID")
    private long gameId;

    @Id
    @Column(name= "GAME_GROUPS_ID")
    private long gameGroupId;

    @ManyToOne
    @JoinColumn(name = "GAMES_ID")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "GAME_GROUPS_ID")
    private GameGroup gameGroup;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameGroup getGameGroup() {
        return gameGroup;
    }

    public void setGameGroup(GameGroup gameGroup) {
        this.gameGroup = gameGroup;
    }
}
