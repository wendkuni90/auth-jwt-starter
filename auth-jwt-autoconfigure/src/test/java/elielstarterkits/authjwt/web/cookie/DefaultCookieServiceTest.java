package elielstarterkits.authjwt.web.cookie;

import elielstarterkits.authjwt.properties.JwtProperties;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultCookieServiceTest {

    @Test
    void shouldSetAccessCookieWithSameSiteAndHttpOnly() {
        JwtProperties props = new JwtProperties();
        props.setSecret("super-secret-key-super-secret-key-super-secret");
        props.getCookie().setSameSite("Strict");
        props.getCookie().setSecure(true);
        props.getCookie().setHttpOnly(true);
        props.getCookie().setAccessName("ACCESS_TOKEN");
        props.getCookie().setAccessPath("/");

        DefaultCookieService service = new DefaultCookieService(props);

        MockHttpServletResponse response = new MockHttpServletResponse();
        service.setAccessTokenCookie(response, "jwt-value");

        String header = response.getHeader("Set-Cookie");
        assertThat(header).contains("ACCESS_TOKEN=jwt-value");
        assertThat(header).contains("HttpOnly");
        assertThat(header).contains("Secure");
        assertThat(header).contains("SameSite=Strict");
        assertThat(header).contains("Path=/");
    }

    @Test
    void shouldClearRefreshCookieWithMaxAge0() {
        JwtProperties props = new JwtProperties();
        props.setSecret("super-secret-key-super-secret-key-super-secret");
        props.getCookie().setSameSite("Strict");
        props.getCookie().setRefreshName("REFRESH_TOKEN");
        props.getCookie().setRefreshPath("/auth/refresh");

        DefaultCookieService service = new DefaultCookieService(props);

        MockHttpServletResponse response = new MockHttpServletResponse();
        service.clearRefreshTokenCookie(response);

        String header = response.getHeader("Set-Cookie");
        assertThat(header).contains("REFRESH_TOKEN=");
        assertThat(header).contains("Max-Age=0");
        assertThat(header).contains("Path=/auth/refresh");
    }
}
