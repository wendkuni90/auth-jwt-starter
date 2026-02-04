package elielstarterkits.authjwt.token.refresh;

import elielstarterkits.authjwt.properties.JwtProperties;
import elielstarterkits.authjwt.token.TokenBlacklistService;

import java.util.Optional;

public class RefreshTokenRotationService {

    private final JwtProperties props;
    private final RefreshTokenService refreshTokenService;
    private final Optional<TokenBlacklistService> blacklistService;

    public RefreshTokenRotationService(
            JwtProperties props,
            RefreshTokenService refreshTokenService,
            Optional<TokenBlacklistService> blacklistService
    ) {
        this.props = props;
        this.refreshTokenService = refreshTokenService;
        this.blacklistService = blacklistService;
    }

    /**
     * Flow:
     * 1) refresh reçu
     * 2) ancien invalidé (revoke)
     * 3) nouveau généré
     * 4) blacklist ancien jti si activée + impl présente
     */
    public String rotate(String oldRefreshJwt) {
        RefreshToken old = refreshTokenService.validate(oldRefreshJwt);

        // revoke old
        refreshTokenService.revoke(old.getJti());

        // create new
        String newRefreshJwt = refreshTokenService.create(old.getSubject());

        // optionally blacklist old refresh jti (empêche toute réutilisation via jti)
        if (props.getBlacklist().isEnabled() && blacklistService.isPresent()) {
            blacklistService.get().blacklist(old.getJti(), old.getExpiresAt());
        }

        return newRefreshJwt;
    }
}
