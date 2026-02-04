package elielstarterkits.authjwt.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsServiceAdapter {

    /**
     * Charge un UserDetails à partir du "subject" (sub) du token.
     * Typiquement: username ou userId (selon ce que l'app hôte met dans sub).
     */
    UserDetails loadBySubject(String subject);
}
