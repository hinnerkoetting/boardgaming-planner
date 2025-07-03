package de.oetting.wwp.player;

import de.oetting.wwp.entities.Player;
import de.oetting.wwp.exceptions.ConflictException;
import de.oetting.wwp.exceptions.ForbiddenException;
import de.oetting.wwp.player.PlayerRepository;
import de.oetting.wwp.security.Role;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    public void deletePlayerById(@PathVariable("playerId") long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(player.getName());
        checkNotEditingYourself(player);
        if (isOwner(userDetails)) {
            throw new ForbiddenException("Owner cannot be deleted");
        }

        player.getGameGroups().forEach(gameGroup -> gameGroup.deletePlayer(player));

        playerRepository.delete(player);

        userDetailsService.deleteUser(player.getName());
    }



    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    public Iterable<Player> listPlayers() {
        return playerRepository.findAll();
    }

    @GetMapping(path = "/{playerId}")
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    public PlayerDetailsModel getPlayer(@PathVariable("playerId") long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NoSuchElementException("Player not found"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(player.getName());
        PlayerDetailsModel model = new PlayerDetailsModel();
        model.setId(player.getId());
        model.setName(player.getName());
        model.setRoles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        return model;
    }

    private static boolean isOwner(UserDetails userDetails) {
        return userDetails.getAuthorities().stream().anyMatch(authority -> Role.OWNER.getName().equals(authority.getAuthority()));
    }

    private void checkNotEditingYourself( Player player) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (player.getName().equals(authentication.getPrincipal())) {
            throw new ConflictException("Cannot change roles on yourself");
        }
    }

}
