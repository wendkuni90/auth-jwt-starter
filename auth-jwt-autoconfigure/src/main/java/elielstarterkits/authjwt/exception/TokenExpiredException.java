package elielstarterkits.authjwt.exception;

public class TokenExpiredException extends AuthException {

    public TokenExpiredException() {
        super("TOKEN_EXPIRED", "Token has expired.");
    }

    public TokenExpiredException(String message) {
        super("TOKEN_EXPIRED", message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super("TOKEN_EXPIRED", message, cause);
    }
}
