package de.oetting.wwp.controller;

import de.oetting.wwp.service.events.SseEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {

    @Autowired
    private SseEmitterService sseEmitterService;

    @GetMapping("api/sse/gameGroup/{gameGroupId}")
    public SseEmitter subscribeToPing(@PathVariable("gameGroupId") int gameGroupId) {
        return sseEmitterService.createGameGroupEmitter(gameGroupId);
    }

}