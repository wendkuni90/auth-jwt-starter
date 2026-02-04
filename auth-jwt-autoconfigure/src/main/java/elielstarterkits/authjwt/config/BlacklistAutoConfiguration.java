package elielstarterkits.authjwt.config;

import elielstarterkits.authjwt.properties.JwtProperties;
import elielstarterkits.authjwt.token.InMemoryTokenBlacklistService;
import elielstarterkits.authjwt.token.TokenBlacklistService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlacklistAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(TokenBlacklistService.class)
    public TokenBlacklistService tokenBlacklistService(JwtProperties props) {
        // Si blacklist désactivée, on retourne quand même une impl "no-op" ?
        // Ici on reste simple: on fournit l'InMemory, mais le filtre ne l'utilise
        // que si props.blacklist.enabled = true (déjà codé en PHASE 2).
        return new InMemoryTokenBlacklistService();
    }
}
