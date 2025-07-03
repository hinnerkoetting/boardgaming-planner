package de.oetting.wwp.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class GameGroup {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToMany
    private Set<Game> playedGames;

    @ManyToMany
    private Set<Player> players;

    @Column(unique = true)
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Game> getPlayedGames() {
        return playedGames;
    }

    public void setPlayedGames(Set<Game> playedGames) {
        this.playedGames = playedGames;
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
        this.playedGames.add(game);
        game.addGameGroup(this);
    }

    public void deleteGame(Game g) {
        this.playedGames.remove(g);
        g.deleteGameGroup(this);
    }

    public void deletePlayer(Player player) {
        this.players.remove(player);
        player.deleteGameGroup(this);
    }
}
