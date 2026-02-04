package elielstarterkits.authjwt.config;

import elielstarterkits.authjwt.persistence.RefreshTokenRepository;
import elielstarterkits.authjwt.properties.JwtProperties;
import elielstarterkits.authjwt.token.JwtTokenProvider;
import elielstarterkits.authjwt.token.TokenBlacklistService;
import elielstarterkits.authjwt.token.refresh.JpaRefreshTokenService;
import elielstarterkits.authjwt.token.refresh.RefreshTokenRotationService;
import elielstarterkits.authjwt.token.refresh.RefreshTokenService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class JwtCoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenProvider jwtTokenProvider(JwtProperties props) {
        return new JwtTokenProvider(props);
    }

    @Bean
    @ConditionalOnMissingBean(RefreshTokenService.class)
    @ConditionalOnClass(RefreshTokenRepository.class)
    public RefreshTokenService refreshTokenService(JwtProperties props,
                                                    JwtTokenProvider provider,
                                                    RefreshTokenRepository repo) {
        return new JpaRefreshTokenService(props, provider, repo);
    }

    @Bean
    @ConditionalOnMissingBean
    public RefreshTokenRotationService refreshTokenRotationService(JwtProperties props,
                                                                    RefreshTokenService refreshTokenService,
                                                                    Optional<TokenBlacklistService> blacklistService) {
        return new RefreshTokenRotationService(props, refreshTokenService, blacklistService);
    }
}
