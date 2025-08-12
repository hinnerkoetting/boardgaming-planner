package de.oetting.bgp.infrastructure;

import de.oetting.bgp.security.Role;
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

    public static boolean checkIfUserIsGlobalAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(Role.ADMIN.getName()));
    }
    
}
