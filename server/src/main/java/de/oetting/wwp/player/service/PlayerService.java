package de.oetting.wwp.player.service;

import de.oetting.wwp.entities.Player;
import de.oetting.wwp.exceptions.ForbiddenException;
import de.oetting.wwp.player.PlayerRepository;
import de.oetting.wwp.repositories.RatingRepository;
import de.oetting.wwp.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserDetailsManager userDetailsService;

    @Autowired
    private RatingRepository ratingRepository;

    public void delete(Player player) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(player.getName());
        if (isOwner(userDetails)) {
            throw new ForbiddenException("Owner cannot be deleted");
        }
        ratingRepository.deleteByPlayerId(player.getId());
        player.getGameGroups().forEach(gameGroup -> gameGroup.deletePlayer(player));

        playerRepository.delete(player);

        userDetailsService.deleteUser(player.getName());
    }

    private static boolean isOwner(UserDetails userDetails) {
        return userDetails.getAuthorities().stream().anyMatch(authority -> Role.OWNER.getName().equals(authority.getAuthority()));
    }

}
