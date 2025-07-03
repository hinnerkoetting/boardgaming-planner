package de.oetting.wwp.infrastructure;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {

    public static String getCurrentUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static long getCurrentPlayerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Claims claims = (Claims) authentication.getDetails();
        return claims.get("player_id", Long.class);
    }
}
