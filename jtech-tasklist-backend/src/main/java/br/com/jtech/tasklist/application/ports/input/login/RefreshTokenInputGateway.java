package br.com.jtech.tasklist.application.ports.input.login;

import br.com.jtech.tasklist.adapters.input.protocols.login.AuthResponse;

public interface RefreshTokenInputGateway {
    AuthResponse refresh(String refreshToken);
}
