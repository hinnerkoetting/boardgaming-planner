package de.oetting.bgp.security.controller;

import de.oetting.bgp.entities.Player;
import de.oetting.bgp.exceptions.ConflictException;
import de.oetting.bgp.exceptions.ForbiddenException;
import de.oetting.bgp.player.PlayerRepository;
import de.oetting.bgp.security.Role;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players/{playerId}/roles")
public class RolesController {

    private static final Logger LOG = LoggerFactory.getLogger(RolesController.class);

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserDetailsManager userDetailsManager;

    @PostMapping(value = "/bootstrapOwner")
    @Transactional
    public ResponseEntity<?> bootstrapOwner(@PathVariable("playerId") long playerId)  {
        if (System.getenv("BOOTSTRAP_OWNER_ALLOWED") == null) {
            LOG.info("Bootstrap owner not allowed because env BOOTSTRAP_OWNER_ALLOWED is not defined");
            return ResponseEntity.badRequest().build();
        }
        List<String> owners = groupManager.findUsersInGroup("owner");

        if (!owners.isEmpty()) {
            LOG.info("Bootstrap owner not allowed there is already an owner");
            return ResponseEntity.badRequest().build();
        }
        LOG.warn("Trying to bootstrap owner. No owner exists yet!");
        if (groupManager.findAllGroups().stream().noneMatch("owner"::equals)) {
            LOG.warn("Owner group does not exist");
            groupManager.createGroup("owner", Collections.emptyList());
        }
        LOG.warn("Promoting {} to owner", playerId);

        Player player = playerRepository.findById(playerId).orElseThrow(() -> new NoSuchElementException("Player not found"));
        User user = addRoles(player, Arrays.asList(Role.ADMIN, Role.OWNER ));
        groupManager.addUserToGroup(user.getUsername(), "owner");

        return ResponseEntity.ok(null);
    }

    @PutMapping(value = "/{role}")
    @Transactional
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    @ResponseStatus(HttpStatus.OK)
    public void addRole(@PathVariable("playerId") long playerId, @PathVariable("role") String roleName)  {
        Role role = Role.from(roleName);
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new NoSuchElementException("Player not found"));
        checkNotEditingYourself(player);
        checkNotRoleOwner(role);

        addRoles(player, Collections.singleton(role));
    }

    @DeleteMapping(value = "/{role}")
    @Transactional
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    @ResponseStatus(HttpStatus.OK)
    public void deleteRole(@PathVariable("playerId") long playerId, @PathVariable("role") String roleName) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new NoSuchElementException("Player not found"));
        UserDetails userDetails = userDetailsManager.loadUserByUsername(player.getName());
        checkNotEditingYourself(player);
        Role role = Role.from(roleName);
        checkNotRoleOwner(role);
        if (isOwner(userDetails)) {
            throw new ForbiddenException("Cannot delete role from owner");
        }

        deleteRoles(playerId, Collections.singleton(role));
    }

    @GetMapping()
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    @ResponseStatus(HttpStatus.OK)
    public List<String> getRoles(@PathVariable("playerId") long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new NoSuchElementException("Player not found"));

        UserDetails userDetails = userDetailsManager.loadUserByUsername(player.getName());
        return userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    private void checkNotEditingYourself( Player player) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (player.getName().equals(authentication.getPrincipal())) {
            throw new ConflictException("Cannot change roles on yourself");
        }
    }

    private void checkNotRoleOwner(Role role) {
        if (role == Role.OWNER) {
            throw new ConflictException("Cannot change role OWNER");
        }
    }

    private User addRoles(Player player, Collection<Role> roles) {
        UserDetails userDetails = userDetailsManager.loadUserByUsername(player.getName());
        Set<GrantedAuthority> authorities = new HashSet<>(userDetails.getAuthorities());
        roles.stream().map(name -> new SimpleGrantedAuthority(name.getName())).forEach(authorities::add);

        User updatedUser = new User(userDetails.getUsername(), userDetails.getPassword(), authorities);
        userDetailsManager.updateUser(updatedUser);
        return updatedUser;
    }

    private void deleteRoles(long playerId, Collection<Role> roles) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new NoSuchElementException("Player not found"));
        UserDetails userDetails = userDetailsManager.loadUserByUsername(player.getName());
        Set<GrantedAuthority> authorities = new HashSet<>(userDetails.getAuthorities());
        roles.stream().map(name -> new SimpleGrantedAuthority(name.getName())).forEach(authorities::remove);

        User updatedUser = new User(userDetails.getUsername(), userDetails.getPassword(), authorities);
        userDetailsManager.updateUser(updatedUser);
    }

    private static boolean isOwner(UserDetails userDetails) {
        return userDetails.getAuthorities().stream().anyMatch(authority -> Role.OWNER.getName().equals(authority.getAuthority()));
    }

}
