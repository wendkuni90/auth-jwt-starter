package elielstarterkits.authjwt.token.refresh;

public interface RefreshTokenService {

    /**
     * Crée un refresh token pour un subject (userId/username).
     * Retourne le JWT refresh token (string) à envoyer au client.
     */
    String create(String subject);

    /**
     * Valide un refresh token reçu :
     * - JWT valide (signature, exp, issuer, audience, typ)
     * - existe en base
     * - pas révoqué
     */
    RefreshToken validate(String refreshTokenJwt);

    /**
     * Révoque un refresh token (par jti).
     */
    void revoke(String jti);
}
