package br.com.jtech.tasklist.adapters.input.controllers.login;

import br.com.jtech.tasklist.adapters.input.protocols.login.RegisterRequest;
import br.com.jtech.tasklist.adapters.input.protocols.login.UserResponse;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.login.RegisterUserInputGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RegisterUserController {

    private final RegisterUserInputGateway registerUserInputGateway;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        var created = registerUserInputGateway.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.of(created));
    }
}
