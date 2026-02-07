package br.com.jtech.tasklist.adapters.input.controllers.login;

import br.com.jtech.tasklist.adapters.input.protocols.login.AuthResponse;
import br.com.jtech.tasklist.adapters.input.protocols.login.LoginRequest;
import br.com.jtech.tasklist.application.ports.input.login.LoginUserInputGateway;
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
public class LoginUserController {

    private final LoginUserInputGateway loginUserInputGateway;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        var response = loginUserInputGateway.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}
