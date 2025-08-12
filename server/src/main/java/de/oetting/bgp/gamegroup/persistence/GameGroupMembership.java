package de.oetting.bgp.gamegroup.persistence;

import de.oetting.bgp.entities.Player;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "GAME_GROUP_2_PLAYERS")
@IdClass(GameGroup2PlayerId.class)
public class GameGroupMembership {

    @Id
    @Column(name = "PLAYER_ID")
    private long playerId;

    @Id
    @Column(name = "GAME_GROUP_ID")
    private long gameGroupId;

    @ManyToOne
    @JoinColumn(name = "PLAYER_ID", insertable = false, updatable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "GAME_GROUP_ID", insertable = false, updatable = false)
    private GameGroupEntity gameGroup;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private GameGroupMemberType type;

    public GameGroupMembership() {
    }

    public GameGroupMembership(long playerId, long gameGroupId) {
        this.playerId = playerId;
        this.gameGroupId = gameGroupId;
    }

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

    public GameGroupMemberType getType() {
        return type;
    }

    public void setType(GameGroupMemberType type) {
        this.type = type;
    }
}
