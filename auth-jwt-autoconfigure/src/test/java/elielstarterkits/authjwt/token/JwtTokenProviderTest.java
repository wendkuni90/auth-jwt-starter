package elielstarterkits.authjwt.token;

import elielstarterkits.authjwt.exception.TokenExpiredException;
import elielstarterkits.authjwt.properties.JwtProperties;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class JwtTokenProviderTest {

    @Test
    void shouldGenerateAndValidateAccessToken() {
        JwtProperties props = TestJwtPropsFactory.defaultProps();
        JwtTokenProvider provider = new JwtTokenProvider(props);

        String token = provider.generateAccessToken("42", List.of("ROLE_USER"));
        assertThat(provider.validateToken(token)).isTrue();

        var claims = provider.extractClaims(token);
        assertThat(claims.getSubject()).isEqualTo("42");
        assertThat(claims.get("typ", String.class)).isEqualTo("access");
    }

    @Test
    void shouldThrowExpiredForExpiredToken() throws InterruptedException {
        JwtProperties props = TestJwtPropsFactory.defaultProps();
        props.getAccessToken().setExpiration(Duration.ofMillis(300));

        JwtTokenProvider provider = new JwtTokenProvider(props);

        String token = provider.generateAccessToken("42", List.of("ROLE_USER"));
        Thread.sleep(400);

        assertThatThrownBy(() -> provider.extractClaims(token))
                .isInstanceOf(TokenExpiredException.class);
    }
}
