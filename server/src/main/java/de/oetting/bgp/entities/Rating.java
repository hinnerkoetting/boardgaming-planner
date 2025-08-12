package de.oetting.bgp.entities;

import de.oetting.bgp.game.entity.Game;
import de.oetting.bgp.gamegroup.persistence.GameGroupEntity;
import jakarta.persistence.*;

@Entity
public class Rating {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PLAYER_ID", nullable = false, updatable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "GAME_GROUP_ID", nullable = false, updatable = false)
    private GameGroupEntity gameGroup;

    @ManyToOne
    @JoinColumn(name = "GAME_ID", nullable = false, updatable = false)
    private Game game;

    private int rating;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameGroupEntity getGameGroup() {
        return gameGroup;
    }

    public void setGameGroup(GameGroupEntity gameGroup) {
        this.gameGroup = gameGroup;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
