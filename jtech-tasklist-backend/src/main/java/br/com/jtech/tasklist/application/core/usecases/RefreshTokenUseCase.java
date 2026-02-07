package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.adapters.input.protocols.login.AuthResponse;
import br.com.jtech.tasklist.application.ports.input.login.RefreshTokenInputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.InvalidCredentialsException;
import br.com.jtech.tasklist.config.security.JwtTokenProvider;

public class RefreshTokenUseCase implements RefreshTokenInputGateway {

    private final JwtTokenProvider jwtTokenProvider;

    public RefreshTokenUseCase(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidCredentialsException("Invalid or expired refresh token");
        }

        var userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        var email = jwtTokenProvider.getEmailFromToken(refreshToken);

        var newToken = jwtTokenProvider.generateToken(userId, email);
        var newRefreshToken = jwtTokenProvider.generateRefreshToken(userId, email);

        return AuthResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .type("Bearer")
                .build();
    }
}
