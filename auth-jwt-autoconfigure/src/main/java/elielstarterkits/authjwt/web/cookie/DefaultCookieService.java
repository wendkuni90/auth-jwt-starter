package elielstarterkits.authjwt.web.cookie;

import elielstarterkits.authjwt.properties.CookieProperties;
import elielstarterkits.authjwt.properties.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class DefaultCookieService implements CookieService {

    private final JwtProperties props;
    private final CookieProperties cookieProps;

    public DefaultCookieService(JwtProperties props) {
        this.props = props;
        this.cookieProps = props.getCookie();
    }

    @Override
    public void setAccessTokenCookie(HttpServletResponse response, String jwt) {
        ResponseCookie cookie = buildCookie(
                cookieProps.getAccessName(),
                jwt,
                cookieProps.getAccessPath(),
                props.getAccessToken().getExpiration()
        );
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @Override
    public void setRefreshTokenCookie(HttpServletResponse response, String jwt) {
        ResponseCookie cookie = buildCookie(
                cookieProps.getRefreshName(),
                jwt,
                cookieProps.getRefreshPath(),
                props.getRefreshToken().getExpiration()
        );
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @Override
    public void clearAccessTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = clearCookie(cookieProps.getAccessName(), cookieProps.getAccessPath());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @Override
    public void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = clearCookie(cookieProps.getRefreshName(), cookieProps.getRefreshPath());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private ResponseCookie buildCookie(String name, String value, String path, Duration maxAge) {
        var builder = ResponseCookie.from(name, value)
                .httpOnly(cookieProps.isHttpOnly())
                .secure(cookieProps.isSecure())
                .path(path)
                .maxAge(maxAge);

        if (cookieProps.getDomain() != null && !cookieProps.getDomain().isBlank()) {
            builder = builder.domain(cookieProps.getDomain());
        }

        // SameSite: Strict/Lax/None
        builder = builder.sameSite(cookieProps.getSameSite());

        return builder.build();
    }

    private ResponseCookie clearCookie(String name, String path) {
        var builder = ResponseCookie.from(name, "")
                .httpOnly(cookieProps.isHttpOnly())
                .secure(cookieProps.isSecure())
                .path(path)
                .maxAge(0);

        if (cookieProps.getDomain() != null && !cookieProps.getDomain().isBlank()) {
            builder = builder.domain(cookieProps.getDomain());
        }

        builder = builder.sameSite(cookieProps.getSameSite());

        return builder.build();
    }
}
