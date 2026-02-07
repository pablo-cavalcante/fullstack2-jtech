package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.login.RegisterUserInputGateway;
import br.com.jtech.tasklist.application.ports.output.login.RegisterUserOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RegisterUserUseCase implements RegisterUserInputGateway {

    private final RegisterUserOutputGateway registerUserOutputGateway;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(RegisterUserOutputGateway registerUserOutputGateway,
                               PasswordEncoder passwordEncoder) {
        this.registerUserOutputGateway = registerUserOutputGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        if (registerUserOutputGateway.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return registerUserOutputGateway.save(user);
    }
}
