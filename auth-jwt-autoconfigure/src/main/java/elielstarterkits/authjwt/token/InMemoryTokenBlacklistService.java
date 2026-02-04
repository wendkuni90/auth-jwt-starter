package elielstarterkits.authjwt.token;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Blacklist en mémoire (implémentation par défaut).
 * Stocke jti -> expiresAt et purge à la volée.
 *
 * Remplaçable facilement par Redis / DB via @ConditionalOnMissingBean.
 */
public class InMemoryTokenBlacklistService implements TokenBlacklistService {

    private final Map<String, Instant> store = new ConcurrentHashMap<>();

    @Override
    public boolean isBlacklisted(String jti) {
        if (jti == null || jti.isBlank()) return false;

        Instant expiresAt = store.get(jti);
        if (expiresAt == null) return false;

        // Purge opportuniste
        if (expiresAt.isBefore(Instant.now())) {
            store.remove(jti);
            return false;
        }
        return true;
    }

    @Override
    public void blacklist(String jti, Instant expiresAt) {
        if (jti == null || jti.isBlank()) return;
        Objects.requireNonNull(expiresAt, "expiresAt must not be null");

        // Si déjà expiré, inutile de stocker
        if (expiresAt.isBefore(Instant.now())) return;

        store.put(jti, expiresAt);
    }

    /**
     * Optionnel: utile en test/diagnostic.
     */
    int size() {
        purgeExpired();
        return store.size();
    }

    /**
     * Purge manuelle (optionnel). Peut être appelé via une tâche planifiée plus tard si tu veux.
     */
    void purgeExpired() {
        Instant now = Instant.now();
        store.entrySet().removeIf(e -> e.getValue().isBefore(now));
    }
}
