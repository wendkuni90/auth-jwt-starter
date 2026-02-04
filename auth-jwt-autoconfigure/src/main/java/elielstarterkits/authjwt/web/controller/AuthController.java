package elielstarterkits.authjwt.web.controller;

import elielstarterkits.authjwt.properties.JwtProperties;
import elielstarterkits.authjwt.token.JwtTokenProvider;
import elielstarterkits.authjwt.token.TokenBlacklistService;
import elielstarterkits.authjwt.token.refresh.RefreshTokenService;
import elielstarterkits.authjwt.web.cookie.CookieService;
import elielstarterkits.authjwt.web.dto.AuthResponse;
import elielstarterkits.authjwt.web.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtProperties props;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final CookieService cookieService;
    private final Optional<TokenBlacklistService> blacklistService;

    public AuthController(JwtProperties props,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          RefreshTokenService refreshTokenService,
                          CookieService cookieService,
                          Optional<TokenBlacklistService> blacklistService) {
        this.props = props;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.cookieService = cookieService;
        this.blacklistService = blacklistService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletResponse response) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String subject = auth.getName();
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String accessJwt = tokenProvider.generateAccessToken(subject, roles);
        String refreshJwt = refreshTokenService.create(subject);

        cookieService.setAccessTokenCookie(response, accessJwt);
        cookieService.setRefreshTokenCookie(response, refreshJwt);

        return ResponseEntity.ok(AuthResponse.ok("Login successful."));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletRequest request,
                                               HttpServletResponse response) {

        // 1) blacklist access token jti (si activée + impl présente)
        String accessJwt = readCookie(request, props.getCookie().getAccessName());
        if (accessJwt != null && props.getBlacklist().isEnabled() && blacklistService.isPresent()) {
            try {
                var claims = tokenProvider.extractClaims(accessJwt);
                String jti = claims.getId();
                Instant exp = claims.getExpiration().toInstant();
                if (jti != null) {
                    blacklistService.get().blacklist(jti, exp);
                }
            } catch (Exception ignored) {
                // si token invalide/expiré, on ignore : logout doit rester "safe"
            }
        }

        // 2) clear cookies
        cookieService.clearAccessTokenCookie(response);
        cookieService.clearRefreshTokenCookie(response);

        // 3) (optionnel) révoquer refresh côté serveur
        String refreshJwt = readCookie(request, props.getCookie().getRefreshName());
        if (refreshJwt != null) {
            try {
                var refresh = refreshTokenService.validate(refreshJwt);
                refreshTokenService.revoke(refresh.getJti());
            } catch (Exception ignored) {
                // idem
            }
        }

        return ResponseEntity.ok(AuthResponse.ok("Logout successful."));
    }

    private String readCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (var c : request.getCookies()) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }
}
