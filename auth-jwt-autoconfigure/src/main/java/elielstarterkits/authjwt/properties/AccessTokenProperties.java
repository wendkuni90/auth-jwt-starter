package elielstarterkits.authjwt.properties;

import jakarta.validation.constraints.NotNull;

import java.time.Duration;

public class AccessTokenProperties {

    /**
     * Durée de vie de l'access token.
     * Default (décision): 15 minutes.
     */
    @NotNull
    private Duration expiration = Duration.ofMinutes(15);

    public Duration getExpiration() { return expiration; }
    public void setExpiration(Duration expiration) { this.expiration = expiration; }
}
