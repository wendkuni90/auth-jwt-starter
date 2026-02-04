package elielstarterkits.authjwt.persistence;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "auth_refresh_tokens",
        indexes = {
                @Index(name = "idx_refresh_jti", columnList = "jti", unique = true),
                @Index(name = "idx_refresh_subject", columnList = "subject")
        })
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * JWT ID (jti) — unique
     */
    @Column(nullable = false, unique = true, length = 64)
    private String jti;

    /**
     * sub (userId/username). Le starter n'impose pas le modèle User.
     */
    @Column(nullable = false, length = 128)
    private String subject;

    /**
     * Hash SHA-256 du refresh token complet.
     */
    @Column(nullable = false, length = 64)
    private String tokenHash;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked = false;

    /**
     * jti du token qui a remplacé celui-ci (rotation).
     */
    @Column(length = 64)
    private String replacedByJti;

    public RefreshTokenEntity() {}

    // Getters/Setters
    public Long getId() { return id; }

    public String getJti() { return jti; }
    public void setJti(String jti) { this.jti = jti; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public boolean isRevoked() { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }

    public String getReplacedByJti() { return replacedByJti; }
    public void setReplacedByJti(String replacedByJti) { this.replacedByJti = replacedByJti; }
}
