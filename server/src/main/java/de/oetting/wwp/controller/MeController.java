package de.oetting.wwp.controller;

import de.oetting.wwp.controller.model.MeModel;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/me")
public class MeController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping
    public MeModel getInformationAboutMe() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Player player = playerRepository.findByName(username).orElseThrow(() -> new NoSuchElementException("Player not found"));

        MeModel model = new MeModel();
        model.setPlayerId(player.getId());
        model.setName(player.getName());
        return model;
    }
}
