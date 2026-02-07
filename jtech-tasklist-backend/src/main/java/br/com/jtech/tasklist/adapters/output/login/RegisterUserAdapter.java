package br.com.jtech.tasklist.adapters.output.login;

import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.login.RegisterUserOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUserAdapter implements RegisterUserOutputGateway {

    private final UserRepository repository;

    @Override
    public User save(User user) {
        var entity = user.toEntity();
        var saved = repository.save(entity);
        return User.of(saved);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
