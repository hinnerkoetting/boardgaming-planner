package de.oetting.bgp.gamegroup.persistence;

import de.oetting.bgp.entities.Player;
import de.oetting.bgp.game.entity.Game;
import de.oetting.bgp.tags.entity.GameGroupTagEntity;
import de.oetting.bgp.tags.entity.PlayerTagEntity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity(name = "GameGroup")
@Table(name = "GAME_GROUP")
public class GameGroupEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "gameGroup")
    private Set<Game2GameGroupRelation> games;

    @ManyToMany
    private Set<Player> players;

    @ManyToMany(mappedBy = "gameGroup")
    private Set<GameGroupTagEntity> gameGroupTags;

    @ManyToMany(mappedBy = "gameGroup")
    private Set<PlayerTagEntity> playerTags;

    @Column(unique = true)
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private GameGroupType type;

    @Column(name = "open_for_new_players")
    private boolean openForNewPlayers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Game2GameGroupRelation> getGames() {
        return games;
    }

    public void setGames(Set<Game2GameGroupRelation> games) {
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

    public void addGame(Game2GameGroupRelation gameRelation) {
        this.games.add(gameRelation);
        gameRelation.getGame().addGameGroup(gameRelation);
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

    public GameGroupType getType() {
        return type;
    }

    public void setType(GameGroupType type) {
        this.type = type;
    }

    public boolean isOpenForNewPlayers() {
        return openForNewPlayers;
    }

    public void setOpenForNewPlayers(boolean openForNewPlayers) {
        this.openForNewPlayers = openForNewPlayers;
    }
}
