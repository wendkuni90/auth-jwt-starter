package elielstarterkits.authjwt.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtProperties {

    /**
     * Active/désactive le starter.
     */
    private boolean enabled = true;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    /**
     * Secret utilisé pour signer les JWT (HMAC).
     * Recommandation: clé longue (>= 32 chars) en prod.
     */
    @NotBlank
    private String secret;

    /**
     * Issuer du token (claim iss).
     */
    @NotBlank
    private String issuer = "auth-jwt-starter";

    /**
     * Audience du token (claim aud).
     */
    @NotBlank
    private String audience = "api";

    @Valid
    @NotNull
    private AccessTokenProperties accessToken = new AccessTokenProperties();

    @Valid
    @NotNull
    private RefreshTokenProperties refreshToken = new RefreshTokenProperties();

    @Valid
    @NotNull
    private CookieProperties cookie = new CookieProperties();

    @Valid
    @NotNull
    private BlacklistProperties blacklist = new BlacklistProperties();

    // Getters/Setters
    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }

    public String getAudience() { return audience; }
    public void setAudience(String audience) { this.audience = audience; }

    public AccessTokenProperties getAccessToken() { return accessToken; }
    public void setAccessToken(AccessTokenProperties accessToken) { this.accessToken = accessToken; }

    public RefreshTokenProperties getRefreshToken() { return refreshToken; }
    public void setRefreshToken(RefreshTokenProperties refreshToken) { this.refreshToken = refreshToken; }

    public CookieProperties getCookie() { return cookie; }
    public void setCookie(CookieProperties cookie) { this.cookie = cookie; }

    public BlacklistProperties getBlacklist() { return blacklist; }
    public void setBlacklist(BlacklistProperties blacklist) { this.blacklist = blacklist; }
}
