package de.oetting.bgp.security;


import de.oetting.bgp.entities.Player;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class JwtUtil {


    private final String secret_key = Objects.requireNonNull(System.getenv("JWT_SIGNING_KEY"), "env JWT_SIGNING_KEY not defined");

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret_key));
        this.jwtParser = Jwts.parser().verifyWith(key).build();
    }

    public String createToken(Player player, List<String> roles) {
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + Duration.ofDays(1).toMillis());
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret_key));
        return Jwts.builder()
                .subject(player.getName())
                .expiration(tokenValidity)
                .claim("player_id", player.getId())
                .claim("roles", roles)
                .signWith(key)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }


}