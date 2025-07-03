package de.oetting.wwp.service.events;

import de.oetting.wwp.entities.Player;
import de.oetting.wwp.game.entity.Game;
import de.oetting.wwp.infrastructure.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameGroupEventService {

    @Autowired
    private SseEmitterService sseEmitterService;

    public void playerAdded(long gameGroupId, Player addedUser) {
        var event = new GameGroupEvent(CurrentUser.getCurrentPlayerId(), CurrentUser.getCurrentUsername(), gameGroupId, GameGroupEventType.PLAYER_ADDED, String.format("Player %s joined", addedUser.getName()));
        sseEmitterService.onEventInGameGroup(event);
    }

    public void gameAdded(long gameGroupId, Game game) {
        var event = new GameGroupEvent(CurrentUser.getCurrentPlayerId(), CurrentUser.getCurrentUsername(), gameGroupId, GameGroupEventType.GAME_ADDED, String.format("Game %s was added", game.getName()));
        sseEmitterService.onEventInGameGroup(event);
    }

    public void ratingUpdated(long gameGroupId, Game game, long rating) {
        var event = new GameGroupEvent(CurrentUser.getCurrentPlayerId(), CurrentUser.getCurrentUsername(), gameGroupId, GameGroupEventType.RATING, String.format("Game %s was rated with %s", game.getName(), rating));
        sseEmitterService.onEventInGameGroup(event);
    }
}
