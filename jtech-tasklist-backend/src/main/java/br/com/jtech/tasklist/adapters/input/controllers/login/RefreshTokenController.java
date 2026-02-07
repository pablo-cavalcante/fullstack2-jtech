package br.com.jtech.tasklist.adapters.input.controllers.login;

import br.com.jtech.tasklist.adapters.input.protocols.login.AuthResponse;
import br.com.jtech.tasklist.adapters.input.protocols.login.RefreshTokenRequest;
import br.com.jtech.tasklist.application.ports.input.login.RefreshTokenInputGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenInputGateway refreshTokenInputGateway;

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        var response = refreshTokenInputGateway.refresh(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
