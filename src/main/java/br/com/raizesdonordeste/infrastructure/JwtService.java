package br.com.raizesdonordeste.infrastructure;

import br.com.raizesdonordeste.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtService {
    private final byte[] secret;
    private final long expiration;
    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-seconds}") long expiration) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8); this.expiration = expiration;
    }
    public String issue(User user) {
        Instant now = Instant.now();
        return Jwts.builder().subject(user.getEmail()).claim("role", user.getRole().name())
                .issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(expiration)))
                .signWith(Keys.hmacShaKeyFor(secret)).compact();
    }
    public long expiration() { return expiration; }
    public Claims parse(String token) { return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret)).build().parseSignedClaims(token).getPayload(); }
}
