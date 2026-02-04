package elielstarterkits.authjwt.config;

import elielstarterkits.authjwt.properties.JwtProperties;
import elielstarterkits.authjwt.web.cookie.CookieService;
import elielstarterkits.authjwt.web.cookie.DefaultCookieService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CookieService.class)
    public CookieService cookieService(JwtProperties props) {
        return new DefaultCookieService(props);
    }
}
