package elielstarterkits.authjwt.token;

public interface TokenBlacklistService {
    boolean isBlacklisted(String jti);
}
