package de.oetting.wwp.controller;

import de.oetting.wwp.entities.Game;
import de.oetting.wwp.entities.Player;
import de.oetting.wwp.repositories.GameRepository;
import de.oetting.wwp.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserDetailsManager userDetailsService;

    @DeleteMapping(path = "/{playerId}")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public void deletePlayerById(@PathVariable("playerId") long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow();
        player.getGameGroups().forEach(gameGroup -> gameGroup.deletePlayer(player));

        playerRepository.delete(player);

        userDetailsService.deleteUser(player.getName());
    }
}
