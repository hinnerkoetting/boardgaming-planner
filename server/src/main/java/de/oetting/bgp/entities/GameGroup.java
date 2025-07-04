package de.oetting.bgp.entities;

import de.oetting.bgp.game.entity.Game;
import de.oetting.bgp.tags.entity.GameGroupTagEntity;
import de.oetting.bgp.tags.entity.PlayerTagEntity;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class GameGroup {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToMany
    private Set<Game> games;

    @ManyToMany
    private Set<Player> players;

    @ManyToMany(mappedBy = "gameGroup")
    private Set<GameGroupTagEntity> gameGroupTags;

    @ManyToMany(mappedBy = "gameGroup")
    private Set<PlayerTagEntity> playerTags;

    @Column(unique = true)
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        player.addGameGroup(this);
    }

    public void addGame(Game game) {
        this.games.add(game);
        game.addGameGroup(this);
    }

    public void deleteGame(Game g) {
        this.games.remove(g);
        g.deleteGameGroup(this);
    }

    public void deletePlayer(Player player) {
        this.players.remove(player);
        player.deleteGameGroup(this);
    }

    public void addGameGroupTag(GameGroupTagEntity tag) {
        this.gameGroupTags.add(tag);
    }

    public void removeGameGroupTag(GameGroupTagEntity tag) {
        this.gameGroupTags.remove(tag);
    }

    public Set<GameGroupTagEntity> getGameGroupTags() {
        return gameGroupTags;
    }

    public void setGameGroupTags(Set<GameGroupTagEntity> gameGroupTags) {
        this.gameGroupTags = gameGroupTags;
    }

    public void addPlayerTag(PlayerTagEntity tag) {
        this.playerTags.add(tag);
    }

    public void removePlayerTag(PlayerTagEntity tag) {
        this.playerTags.remove(tag);
    }

    public Set<PlayerTagEntity> getPlayerTags() {
        return playerTags;
    }

    public void setPlayerTags(Set<PlayerTagEntity> playerTags) {
        this.playerTags = playerTags;
    }
}
