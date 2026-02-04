package elielstarterkits.authjwt.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class DefaultUserDetails implements UserDetails {

    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    public DefaultUserDetails(String username) {
        this(username, Collections.emptyList());
    }

    public DefaultUserDetails(String username, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities == null ? Collections.emptyList() : authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Starter : on ne gère pas le mot de passe ici.
     * L'auth réelle se fait via AuthenticationManager côté /auth/login.
     */
    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
