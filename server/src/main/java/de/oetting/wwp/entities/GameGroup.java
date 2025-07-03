package de.oetting.wwp.entities;

import de.oetting.wwp.game.entity.Game;
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
    private Set<GameGroupTag> tags;

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

    public void addGameGroupTag(GameGroupTag tag) {
        this.tags.add(tag);
    }

    public void removeGameGroupTag(GameGroupTag tag) {
        this.tags.remove(tag);
    }

    public Set<GameGroupTag> getTags() {
        return tags;
    }

    public void setTags(Set<GameGroupTag> tags) {
        this.tags = tags;
    }
}
