package br.com.jtech.tasklist.application.ports.output.login;

import br.com.jtech.tasklist.application.core.domains.User;

public interface RegisterUserOutputGateway {
    User save(User user);
    boolean existsByEmail(String email);
}
