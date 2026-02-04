package elielstarterkits.authjwt.web.dto;

public class AuthResponse {

    private final String message;

    public AuthResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }

    public static AuthResponse ok(String message) {
        return new AuthResponse(message);
    }
}
