package elielstarterkits.authjwt.security;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestProtectedController {

    @GetMapping("/protected")
    public String protectedEndpoint(Authentication authentication) {
        return "ok:" + authentication.getName();
    }
}
