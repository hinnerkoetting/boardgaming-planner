package de.oetting.wwp.controller;

import de.oetting.wwp.controller.model.MeModel;
import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/me")
public class MeController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping
    public MeModel getInformationAboutMe() {
        var player = findMyPlayer();

        MeModel model = new MeModel();
        model.setPlayerId(player.getId());
        model.setName(player.getName());
        return model;
    }

    @GetMapping(path = "/gameGroups")
    public Collection<GameGroup> findMyGroups() {
        var player = findMyPlayer();
        return player.getGameGroups();
    }

    private Player findMyPlayer() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playerRepository.findByName(username).orElseThrow(() -> new NoSuchElementException("Player not found"));
    }
}
