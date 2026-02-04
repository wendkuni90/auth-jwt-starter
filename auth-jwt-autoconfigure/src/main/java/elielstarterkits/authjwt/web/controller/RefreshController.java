package elielstarterkits.authjwt.web.controller;

import elielstarterkits.authjwt.properties.JwtProperties;
import elielstarterkits.authjwt.token.JwtTokenProvider;
import elielstarterkits.authjwt.token.refresh.RefreshTokenRotationService;
import elielstarterkits.authjwt.token.refresh.RefreshTokenService;
import elielstarterkits.authjwt.web.cookie.CookieService;
import elielstarterkits.authjwt.web.dto.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class RefreshController {

    private final JwtProperties props;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRotationService rotationService;
    private final JwtTokenProvider tokenProvider;
    private final CookieService cookieService;

    public RefreshController(JwtProperties props,
                            RefreshTokenService refreshTokenService,
                            RefreshTokenRotationService rotationService,
                            JwtTokenProvider tokenProvider,
                            CookieService cookieService) {
        this.props = props;
        this.refreshTokenService = refreshTokenService;
        this.rotationService = rotationService;
        this.tokenProvider = tokenProvider;
        this.cookieService = cookieService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request,
                                                HttpServletResponse response) {

        String refreshJwt = readCookie(request, props.getCookie().getRefreshName());
        if (refreshJwt == null || refreshJwt.isBlank()) {
            return ResponseEntity.status(401).body(AuthResponse.ok("Missing refresh token."));
        }

        // validate old refresh
        var refresh = refreshTokenService.validate(refreshJwt);

        // rotate refresh (invalidate old + issue new)
        String newRefreshJwt = rotationService.rotate(refreshJwt);

        // create new access token
        // roles are not stored in refresh token, so access roles will be empty unless app re-loads them.
        // For now: empty roles (simple). Later we can optionally reload authorities via a hook.
        String newAccessJwt = tokenProvider.generateAccessToken(refresh.getSubject(), List.of());

        cookieService.setAccessTokenCookie(response, newAccessJwt);
        cookieService.setRefreshTokenCookie(response, newRefreshJwt);

        return ResponseEntity.ok(AuthResponse.ok("Token refreshed."));
    }

    private String readCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (var c : request.getCookies()) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }
}
