package elielstarterkits.authjwt.web.cookie;

import jakarta.servlet.http.HttpServletResponse;

public interface CookieService {

    void setAccessTokenCookie(HttpServletResponse response, String jwt);

    void setRefreshTokenCookie(HttpServletResponse response, String jwt);

    void clearAccessTokenCookie(HttpServletResponse response);

    void clearRefreshTokenCookie(HttpServletResponse response);
}
