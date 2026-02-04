package elielstarterkits.authjwt.properties;

import jakarta.validation.constraints.NotNull;

import java.time.Duration;

public class RefreshTokenProperties {

    /**
     * Durée de vie du refresh token.
     * Default (décision): 7 jours.
     */
    @NotNull
    private Duration expiration = Duration.ofDays(7);

    /**
     * Rotation à chaque refresh (décision: true par défaut).
     */
    private boolean rotate = true;

    public Duration getExpiration() { return expiration; }
    public void setExpiration(Duration expiration) { this.expiration = expiration; }

    public boolean isRotate() { return rotate; }
    public void setRotate(boolean rotate) { this.rotate = rotate; }
}
