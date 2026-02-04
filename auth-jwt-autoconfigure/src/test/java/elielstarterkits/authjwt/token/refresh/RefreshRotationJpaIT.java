package elielstarterkits.authjwt.token.refresh;

import elielstarterkits.authjwt.persistence.RefreshTokenRepository;
import elielstarterkits.authjwt.properties.JwtProperties;
import elielstarterkits.authjwt.token.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = RefreshRotationJpaIT.TestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class RefreshRotationJpaIT {

    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRotationService rotationService;
    private final RefreshTokenRepository repo;

    RefreshRotationJpaIT(RefreshTokenService refreshTokenService,
                        RefreshTokenRotationService rotationService,
                        RefreshTokenRepository repo) {
        this.refreshTokenService = refreshTokenService;
        this.rotationService = rotationService;
        this.repo = repo;
    }

    @EnableConfigurationProperties(JwtProperties.class)
    static class TestConfig {

        @org.springframework.context.annotation.Bean
        JwtTokenProvider jwtTokenProvider(JwtProperties props) {
            return new JwtTokenProvider(props);
        }

        @org.springframework.context.annotation.Bean
        RefreshTokenService refreshTokenService(JwtProperties props,
                                                JwtTokenProvider provider,
                                                RefreshTokenRepository repo) {
            return new JpaRefreshTokenService(props, provider, repo);
        }

        @org.springframework.context.annotation.Bean
        RefreshTokenRotationService rotationService(JwtProperties props,
                                                    RefreshTokenService service) {
            return new RefreshTokenRotationService(props, service, Optional.empty());
        }
    }

    @Test
    void shouldRotateAndRevokeOldRefreshToken() {
        String oldRefresh = refreshTokenService.create("42");

        var oldValidated = refreshTokenService.validate(oldRefresh);
        assertThat(repo.findByJti(oldValidated.getJti())).isPresent();

        String newRefresh = rotationService.rotate(oldRefresh);

        // old should now be revoked -> validate should fail
        assertThatThrownBy(() -> refreshTokenService.validate(oldRefresh))
                .isInstanceOfAny(RuntimeException.class);

        // new should validate
        var newValidated = refreshTokenService.validate(newRefresh);
        assertThat(newValidated.getSubject()).isEqualTo("42");
    }
}
