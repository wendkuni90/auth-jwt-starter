package elielstarterkits.authjwt.security;

import elielstarterkits.authjwt.properties.JwtProperties;
import elielstarterkits.authjwt.token.JwtTokenProvider;
import elielstarterkits.authjwt.token.TestJwtPropsFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterUnitTest {

    private JwtProperties props;
    private JwtTokenProvider tokenProvider;
    private JwtAuthenticationFilter filter;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        props = TestJwtPropsFactory.defaultProps();
        tokenProvider = new JwtTokenProvider(props);
        filter = new JwtAuthenticationFilter(props, tokenProvider, Optional.empty());
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateWithValidAccessToken() throws Exception {
        String token = tokenProvider.generateAccessToken("user123", List.of("ROLE_USER"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Cookie cookie = new Cookie(props.getCookie().getAccessName(), token);
        request.setCookies(cookie);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("user123");

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWithoutToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        verify(filterChain).doFilter(request, response);
    }
}
