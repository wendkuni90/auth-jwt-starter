package elielstarterkits.authjwt.properties;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtPropertiesBindingTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(TestConfig.class))
        .withPropertyValues(
            "auth.jwt.secret=super-secret-key-super-secret-key",
            "auth.jwt.issuer=my-issuer",
            "auth.jwt.audience=my-audience",
            "auth.jwt.access-token.expiration=10m",
            "auth.jwt.refresh-token.expiration=14d",
            "auth.jwt.refresh-token.rotate=true",
            "auth.jwt.cookie.same-site=Strict",
            "auth.jwt.blacklist.enabled=true"
        );

    @EnableConfigurationProperties(JwtProperties.class)
    static class TestConfig { }

    @Test
    void shouldBindJwtPropertiesCorrectly() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(JwtProperties.class);

            JwtProperties props = context.getBean(JwtProperties.class);

            assertThat(props.getSecret()).isEqualTo("super-secret-key-super-secret-key");
            assertThat(props.getIssuer()).isEqualTo("my-issuer");
            assertThat(props.getAudience()).isEqualTo("my-audience");

            assertThat(props.getAccessToken().getExpiration()).isEqualTo(Duration.ofMinutes(10));
            assertThat(props.getRefreshToken().getExpiration()).isEqualTo(Duration.ofDays(14));
            assertThat(props.getRefreshToken().isRotate()).isTrue();

            assertThat(props.getCookie().getAccessName()).isEqualTo("ACCESS_TOKEN");
            assertThat(props.getCookie().getRefreshName()).isEqualTo("REFRESH_TOKEN");
            assertThat(props.getCookie().getSameSite()).isEqualTo("Strict");

            assertThat(props.getBlacklist().isEnabled()).isTrue();
        });
    }


}
