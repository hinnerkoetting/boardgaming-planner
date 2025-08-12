package de.oetting.bgp.controller;

import de.oetting.bgp.controller.model.MeModel;
import de.oetting.bgp.entities.Player;
import de.oetting.bgp.exceptions.UnprocessableEntityException;
import de.oetting.bgp.gamegroup.model.GameGroupModel;
import de.oetting.bgp.gamegroup.model.GameGroupModelMapper;
import de.oetting.bgp.gamegroup.persistence.GameGroupType;
import de.oetting.bgp.player.persistence.PlayerRepository;
import de.oetting.bgp.player.service.PlayerService;
import de.oetting.bgp.security.JwtUtil;
import de.oetting.bgp.security.LoginResponse;
import de.oetting.bgp.security.model.UpdateNameRequest;
import de.oetting.bgp.security.model.UpdatePasswordRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/me")
public class MeController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PlayerService playerService;

    @GetMapping
    @Transactional
    public MeModel getInformationAboutMe() {
        var player = findMyPlayer();

        MeModel model = new MeModel();
        model.setPlayerId(player.getId());
        model.setName(player.getName());
        return model;
    }

    @GetMapping(path = "/gameGroups")
    @Transactional
    public Collection<GameGroupModel> findMyGroups() {
        var player = findMyPlayer();
        return player.getGameGroups().stream()
                .map(gameGroupMembership -> GameGroupModelMapper.map(gameGroupMembership.getGameGroup()))
                .filter(group -> group.getType() != GameGroupType.PERSONAL)
                .toList();
    }

    @PutMapping(path = "/name")
    @Transactional
    public LoginResponse updateName(@RequestBody UpdateNameRequest request) {
        var player = findMyPlayer();
        if (player.getName().equals(request.getNewName())) {
            return new LoginResponse(player.getName(), null, player.getId());
        }
        var oldName = player.getName();
        player.setName(request.getNewName());
        UserDetails userDetails = userDetailsManager.loadUserByUsername(oldName);

        var updatedUser = new User(request.getNewName(),
                userDetails.getPassword(),
                userDetails.isEnabled(),
                userDetails.isAccountNonExpired(),
                userDetails.isCredentialsNonExpired(),
                userDetails.isAccountNonLocked(),
                userDetails.getAuthorities()
        );
        userDetailsManager.createUser(updatedUser);
        userDetailsManager.deleteUser(oldName);

        String token = jwtUtil.createToken(player, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        return new LoginResponse(request.getNewName(), token, player.getId());
    }

    @PutMapping(path = "/password")
    @Transactional
    public void updatePassword(@RequestBody UpdatePasswordRequest request) {
        var player = findMyPlayer();
        var authenticationToken = new UsernamePasswordAuthenticationToken(player.getName(), request.getOldPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new UnprocessableEntityException("Authentication failed");
        }

        var newPassword = passwordEncoder.encode(request.getNewPassword());

        UserDetails userDetails = userDetailsManager.loadUserByUsername(player.getName());

        var updatedUser = new User(player.getName(),
                newPassword,
                userDetails.isEnabled(),
                userDetails.isAccountNonExpired(),
                userDetails.isCredentialsNonExpired(),
                userDetails.isAccountNonLocked(),
                userDetails.getAuthorities()
        );
        userDetailsManager.updateUser(updatedUser);
    }

    @DeleteMapping
    @Transactional
    public void delete() {
        var player = findMyPlayer();
        playerService.delete(player);
    }

    private Player findMyPlayer() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playerRepository.findByName(username).orElseThrow(() -> new NoSuchElementException("Player not found"));
    }
}
