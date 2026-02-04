package elielstarterkits.authjwt.token;

import java.time.Instant;

public interface TokenBlacklistService {

    boolean isBlacklisted(String jti);

    /**
     * Ajoute un jti à la blacklist jusqu'à expiresAt (TTL).
     * Impl par défaut (PHASE 4) : InMemory / Redis / DB.
     */
    void blacklist(String jti, Instant expiresAt);
}
