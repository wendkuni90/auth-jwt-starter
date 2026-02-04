package elielstarterkits.authjwt.user;

import org.springframework.security.core.userdetails.UserDetails;

public class NoOpUserDetailsServiceAdapter implements UserDetailsServiceAdapter {

    @Override
    public UserDetails loadBySubject(String subject) {
        // Ne force rien : roles vides
        return new DefaultUserDetails(subject);
    }
}
