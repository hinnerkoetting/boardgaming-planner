package de.oetting.wwp.security;


import de.oetting.wwp.entities.Player;
import de.oetting.wwp.infrastructure.HttpErrorResponse;
import de.oetting.wwp.repositories.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsManager userDetailsManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PlayerRepository playerRepository;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginReq)  {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getLogin(), loginReq.getPassword()));
            String username = authentication.getName();
            UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

            Player player = playerRepository.findByName(loginReq.getLogin()).orElseThrow(() -> new NoSuchElementException("Player not found"));
            String token = jwtUtil.createToken(player, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

            LoginResponse loginRes = new LoginResponse(username, token, player.getId());

            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            HttpErrorResponse errorResponse = new HttpErrorResponse();
            errorResponse.setType("CLIENT_ERROR");
            errorResponse.setDetail("Invalid username or password");
            errorResponse.setTitle("Could not login");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            LOG.error("error during login", e);
            HttpErrorResponse errorResponse = new HttpErrorResponse();
            errorResponse.setType("SERVERT_ERROR");
            errorResponse.setDetail("Unknown error");
            errorResponse.setTitle("Could not login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest)  {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (doesUserAlreadyExist(registrationRequest.getLogin())) {
            return ResponseEntity.badRequest().build();
        }
        userDetailsManager.createUser(new User(registrationRequest.getLogin(), passwordEncoder.encode(registrationRequest.getPassword()), authorities));

        Player player = new Player();
        player.setName(registrationRequest.getLogin());
        Player savedPlayer = playerRepository.save(player);

        String token = jwtUtil.createToken(savedPlayer, authorities.stream().map(GrantedAuthority::getAuthority).toList());

        LoginResponse loginResponse = new LoginResponse(registrationRequest.getLogin(), token, savedPlayer.getId());

        return ResponseEntity.ok(loginResponse);
    }

    private boolean doesUserAlreadyExist(String name) {
        try {
            userDetailsManager.loadUserByUsername(name);
            return true;
        } catch (UsernameNotFoundException e){
            return false;
        }
    }

}