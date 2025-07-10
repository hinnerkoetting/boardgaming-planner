package de.oetting.bgp.controller;

import de.oetting.bgp.infrastructure.CurrentUser;
import de.oetting.bgp.gamegroup.GameGroupRepository;
import de.oetting.bgp.service.events.SseEmitterService;
import okhttp3.internal.platform.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {

    @Autowired
    private SseEmitterService sseEmitterService;

    @Autowired
    private GameGroupRepository gameGroupRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @GetMapping("api/sse/gameGroup/{gameGroupId}")
    public SseEmitter subscribeToGameGroup(@PathVariable("gameGroupId") int gameGroupId) {
        var partOfGroup = checkIsPartOfGroupInOwnConnection(gameGroupId);
        if (!partOfGroup) {
            return null;
        }
        return sseEmitterService.createGameGroupEmitter(gameGroupId);
    }

    private boolean checkIsPartOfGroupInOwnConnection(int gameGroupId) {
        var transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute((_) -> gameGroupRepository.playerAssignedToGameGroup(CurrentUser.getCurrentPlayerId(), gameGroupId).isPresent()) == Boolean.TRUE;
    }

}