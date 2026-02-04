package elielstarterkits.authjwt.exception;

public class InvalidTokenException extends AuthException {

    public InvalidTokenException() {
        super("TOKEN_INVALID", "Token is invalid.");
    }

    public InvalidTokenException(String message) {
        super("TOKEN_INVALID", message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super("TOKEN_INVALID", message, cause);
    }
}
