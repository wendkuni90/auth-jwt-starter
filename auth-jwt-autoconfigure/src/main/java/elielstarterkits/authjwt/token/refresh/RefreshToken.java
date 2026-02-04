package elielstarterkits.authjwt.token.refresh;

import java.time.Instant;

public class RefreshToken {

    private final String jti;
    private final String subject;     // userId/username (sub)
    private final Instant expiresAt;

    public RefreshToken(String jti, String subject, Instant expiresAt) {
        this.jti = jti;
        this.subject = subject;
        this.expiresAt = expiresAt;
    }

    public String getJti() { return jti; }
    public String getSubject() { return subject; }
    public Instant getExpiresAt() { return expiresAt; }
}
