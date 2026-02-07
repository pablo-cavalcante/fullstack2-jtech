package br.com.jtech.tasklist.config.usecases.login;

import br.com.jtech.tasklist.adapters.output.login.RegisterUserAdapter;
import br.com.jtech.tasklist.application.core.usecases.RegisterUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class RegisterUserUseCaseConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(RegisterUserAdapter registerUserAdapter,
                                                    PasswordEncoder passwordEncoder) {
        return new RegisterUserUseCase(registerUserAdapter, passwordEncoder);
    }
}
