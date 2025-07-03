package de.oetting.wwp.security;


import de.oetting.wwp.entities.Player;
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
            User user = new User(loginReq.getLogin(), "", userDetails.getAuthorities());
            String token = jwtUtil.createToken(user);
            LoginResponse loginRes = new LoginResponse(username, token);

            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            LOG.error("error during login", e);
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
        playerRepository.save(player);

        User user = new User(registrationRequest.getLogin(), "", authorities);
        String token = jwtUtil.createToken(user);

        RegistrationResponse registrationResponse = new RegistrationResponse(registrationRequest.getLogin(), token);

        return ResponseEntity.ok(registrationResponse);
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