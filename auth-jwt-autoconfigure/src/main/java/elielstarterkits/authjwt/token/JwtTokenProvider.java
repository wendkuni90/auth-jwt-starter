package elielstarterkits.authjwt.token;

import elielstarterkits.authjwt.exception.InvalidTokenException;
import elielstarterkits.authjwt.exception.TokenExpiredException;
import elielstarterkits.authjwt.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JwtTokenProvider {

    private final JwtProperties props;
    private final SecretKey key;

    public JwtTokenProvider(JwtProperties props) {
        this.props = props;
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String subject, List<String> roles) {
        return buildToken(subject, "access", props.getAccessToken().getExpiration().toSeconds(), Map.of(
                "roles", roles
        ));
    }

    public String generateRefreshToken(String subject) {
        return buildToken(subject, "refresh", props.getRefreshToken().getExpiration().toSeconds(), Map.of());
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (TokenExpiredException | InvalidTokenException ex) {
            return false;
        }
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .requireIssuer(props.getIssuer())
                    .requireAudience(props.getAudience())
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Token has expired.", e);
        } catch (Exception e) {
            throw new InvalidTokenException("Token is invalid.", e);
        }
    }

    private String buildToken(String subject, String typ, long expirationSeconds, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationSeconds);

        String jti = UUID.randomUUID().toString();

        Map<String, Object> allClaims = new HashMap<>(extraClaims);
        allClaims.put("typ", typ);

        Claims claims = Jwts.claims()
                .subject(subject)
                .issuer(props.getIssuer())
                .audience().add(props.getAudience()).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .id(jti)
                .add(allClaims)
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }
}
