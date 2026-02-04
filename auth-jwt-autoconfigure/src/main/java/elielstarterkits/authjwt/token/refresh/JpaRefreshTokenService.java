package elielstarterkits.authjwt.token.refresh;

import elielstarterkits.authjwt.exception.InvalidTokenException;
import elielstarterkits.authjwt.persistence.RefreshTokenEntity;
import elielstarterkits.authjwt.persistence.RefreshTokenRepository;
import elielstarterkits.authjwt.properties.JwtProperties;
import elielstarterkits.authjwt.token.JwtTokenProvider;
import elielstarterkits.authjwt.token.TokenHasher;
import io.jsonwebtoken.Claims;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

public class JpaRefreshTokenService implements RefreshTokenService {

    private final JwtProperties props;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository repo;

    public JpaRefreshTokenService(JwtProperties props, JwtTokenProvider tokenProvider, RefreshTokenRepository repo) {
        this.props = props;
        this.tokenProvider = tokenProvider;
        this.repo = repo;
    }

    @Override
    @Transactional
    public String create(String subject) {
        String refreshJwt = tokenProvider.generateRefreshToken(subject);
        Claims claims = tokenProvider.extractClaims(refreshJwt);

        String typ = claims.get("typ", String.class);
        if (!"refresh".equals(typ)) {
            throw new IllegalStateException("JwtTokenProvider generated non-refresh token for refresh creation.");
        }

        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setJti(claims.getId());
        entity.setSubject(claims.getSubject());
        entity.setExpiresAt(toInstant(claims.getExpiration()));
        entity.setTokenHash(TokenHasher.sha256(refreshJwt));
        entity.setRevoked(false);

        repo.save(entity);
        return refreshJwt;
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken validate(String refreshTokenJwt) {
        // 1) JWT signature + exp + issuer + audience
        Claims claims = tokenProvider.extractClaims(refreshTokenJwt);

        // 2) typ must be refresh
        String typ = claims.get("typ", String.class);
        if (!"refresh".equals(typ)) {
            throw new InvalidTokenException("Invalid token type.");
        }

        String jti = claims.getId();
        if (jti == null || jti.isBlank()) {
            throw new InvalidTokenException("Missing jti.");
        }

        RefreshTokenEntity entity = repo.findByJti(jti)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found."));

        if (entity.isRevoked()) {
            throw new InvalidTokenException("Refresh token revoked.");
        }

        // 3) Compare hash to ensure token hasn't been swapped
        String providedHash = TokenHasher.sha256(refreshTokenJwt);
        if (!providedHash.equals(entity.getTokenHash())) {
            throw new InvalidTokenException("Refresh token mismatch.");
        }

        // 4) Check expiration from DB (authoritative)
        if (entity.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidTokenException("Refresh token expired.");
        }

        return new RefreshToken(entity.getJti(), entity.getSubject(), entity.getExpiresAt());
    }

    @Override
    @Transactional
    public void revoke(String jti) {
        RefreshTokenEntity entity = repo.findByJti(jti)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found."));
        entity.setRevoked(true);
        repo.save(entity);
    }

    @Transactional
    public void markReplaced(String oldJti, String newJti) {
        RefreshTokenEntity entity = repo.findByJti(oldJti)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found."));
        entity.setRevoked(true);
        entity.setReplacedByJti(newJti);
        repo.save(entity);
    }

    private static Instant toInstant(java.util.Date date) {
        return date.toInstant();
    }
}
