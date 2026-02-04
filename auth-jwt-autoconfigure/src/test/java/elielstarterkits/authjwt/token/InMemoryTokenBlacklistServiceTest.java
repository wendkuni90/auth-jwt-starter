package elielstarterkits.authjwt.token;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryTokenBlacklistServiceTest {

    @Test
    void shouldBlacklistAndReturnTrueBeforeExpiration() {
        InMemoryTokenBlacklistService service = new InMemoryTokenBlacklistService();

        String jti = "jti-123";
        service.blacklist(jti, Instant.now().plusSeconds(60));

        assertThat(service.isBlacklisted(jti)).isTrue();
    }

    @Test
    void shouldReturnFalseAfterExpirationAndPurgeEntry() throws Exception {
        InMemoryTokenBlacklistService service = new InMemoryTokenBlacklistService();

        String jti = "jti-expired";
        service.blacklist(jti, Instant.now().plusMillis(150));

        Thread.sleep(200);

        assertThat(service.isBlacklisted(jti)).isFalse();
        assertThat(service.size()).isEqualTo(0);
    }

    @Test
    void shouldIgnoreBlankJti() {
        InMemoryTokenBlacklistService service = new InMemoryTokenBlacklistService();

        service.blacklist("   ", Instant.now().plusSeconds(60));
        assertThat(service.isBlacklisted("   ")).isFalse();
    }
}
