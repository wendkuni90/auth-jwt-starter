package elielstarterkits.authjwt.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SpringUserDetailsServiceAdapter implements UserDetailsServiceAdapter {

    private final UserDetailsService userDetailsService;

    public SpringUserDetailsServiceAdapter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public UserDetails loadBySubject(String subject) {
        // Convention: subject = username
        return userDetailsService.loadUserByUsername(subject);
    }
}
