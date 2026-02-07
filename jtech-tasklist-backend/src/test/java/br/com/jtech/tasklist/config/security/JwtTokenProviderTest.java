package br.com.jtech.tasklist.config.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(
                "test-secret-key-for-jwt-token-generation-minimum-256-bits-test",
                86400000L,
                604800000L
        );
    }

    @Test
    @DisplayName("Should generate a valid access token")
    void shouldGenerateValidAccessToken() {
        var token = jwtTokenProvider.generateToken("user-id-123", "user@test.com");

        assertThat(token).isNotNull().isNotEmpty();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("Should generate a valid refresh token")
    void shouldGenerateValidRefreshToken() {
        var token = jwtTokenProvider.generateRefreshToken("user-id-123", "user@test.com");

        assertThat(token).isNotNull().isNotEmpty();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("Should extract user id from token")
    void shouldExtractUserIdFromToken() {
        var token = jwtTokenProvider.generateToken("user-id-123", "user@test.com");
        var userId = jwtTokenProvider.getUserIdFromToken(token);

        assertThat(userId).isEqualTo("user-id-123");
    }

    @Test
    @DisplayName("Should extract email from token")
    void shouldExtractEmailFromToken() {
        var token = jwtTokenProvider.generateToken("user-id-123", "user@test.com");
        var email = jwtTokenProvider.getEmailFromToken(token);

        assertThat(email).isEqualTo("user@test.com");
    }

    @Test
    @DisplayName("Should return false for invalid token")
    void shouldReturnFalseForInvalidToken() {
        assertThat(jwtTokenProvider.validateToken("invalid-token")).isFalse();
    }

    @Test
    @DisplayName("Should return false for expired token")
    void shouldReturnFalseForExpiredToken() {
        var expiredProvider = new JwtTokenProvider(
                "test-secret-key-for-jwt-token-generation-minimum-256-bits-test",
                -1000L,
                -1000L
        );

        var token = expiredProvider.generateToken("user-id", "email@test.com");
        assertThat(jwtTokenProvider.validateToken(token)).isFalse();
    }

    @Test
    @DisplayName("Should return false for null token")
    void shouldReturnFalseForNullToken() {
        assertThat(jwtTokenProvider.validateToken(null)).isFalse();
    }
}
