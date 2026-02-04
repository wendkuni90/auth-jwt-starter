package elielstarterkits.authjwt.config;

import elielstarterkits.authjwt.properties.JwtProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = "auth.jwt", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({
        // ordre logique : core -> services -> web/security
        JwtCoreAutoConfiguration.class,
        BlacklistAutoConfiguration.class,
        CookieAutoConfiguration.class,
        UserBridgeAutoConfiguration.class,
        SecurityAutoConfiguration.class
})
public class JwtAutoConfiguration {
}
