package de.oetting.bgp.gamingevent.entity;

import de.oetting.bgp.game.entity.Game;
import de.oetting.bgp.gamingevent.GameEventStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "GAME_2_GAMING_EVENT")
public class GamingEventGameEntity {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="GAMING_EVENT_ID", nullable=false, updatable=false)
    private GamingEventEntity gamingEvent;

    @ManyToOne
    @JoinColumn(name="GAME_ID", nullable=false, updatable=false)
    private Game game;

    @Enumerated(EnumType.STRING)
    @Column(name = "GAME_STATUS")
    private GameEventStatus gameStatus;

    @Column(name = "COMMENT")
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GamingEventEntity getGamingEvent() {
        return gamingEvent;
    }

    public void setGamingEvent(GamingEventEntity gamingEvent) {
        this.gamingEvent = gamingEvent;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameEventStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameEventStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
