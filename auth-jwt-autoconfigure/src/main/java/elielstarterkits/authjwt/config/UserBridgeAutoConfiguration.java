package elielstarterkits.authjwt.config;

import elielstarterkits.authjwt.user.NoOpUserDetailsServiceAdapter;
import elielstarterkits.authjwt.user.SpringUserDetailsServiceAdapter;
import elielstarterkits.authjwt.user.UserDetailsServiceAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserBridgeAutoConfiguration {

    /**
     * Si l'app hôte a un UserDetailsService, on l'utilise (cas standard).
     */
    @Bean
    @ConditionalOnClass(UserDetailsService.class)
    @ConditionalOnMissingBean(UserDetailsServiceAdapter.class)
    public UserDetailsServiceAdapter userDetailsServiceAdapter(UserDetailsService uds) {
        return new SpringUserDetailsServiceAdapter(uds);
    }

    /**
     * Sinon, on fournit un fallback (roles vides).
     * L'app hôte peut override en déclarant son propre bean UserDetailsServiceAdapter.
     */
    @Bean
    @ConditionalOnMissingBean(UserDetailsServiceAdapter.class)
    public UserDetailsServiceAdapter noOpUserDetailsServiceAdapter() {
        return new NoOpUserDetailsServiceAdapter();
    }
}
