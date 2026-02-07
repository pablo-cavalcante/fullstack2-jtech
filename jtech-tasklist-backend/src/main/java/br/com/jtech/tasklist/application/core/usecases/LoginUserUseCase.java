package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.adapters.input.protocols.login.AuthResponse;
import br.com.jtech.tasklist.application.ports.input.login.LoginUserInputGateway;
import br.com.jtech.tasklist.application.ports.output.login.FindUserByEmailOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.InvalidCredentialsException;
import br.com.jtech.tasklist.config.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginUserUseCase implements LoginUserInputGateway {

    private final FindUserByEmailOutputGateway findUserByEmailOutputGateway;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginUserUseCase(FindUserByEmailOutputGateway findUserByEmailOutputGateway,
                            PasswordEncoder passwordEncoder,
                            JwtTokenProvider jwtTokenProvider) {
        this.findUserByEmailOutputGateway = findUserByEmailOutputGateway;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponse login(String email, String password) {
        var user = findUserByEmailOutputGateway.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        var token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
        var refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .type("Bearer")
                .build();
    }
}
