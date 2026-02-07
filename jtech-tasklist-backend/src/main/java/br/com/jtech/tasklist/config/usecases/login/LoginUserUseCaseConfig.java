package br.com.jtech.tasklist.config.usecases.login;

import br.com.jtech.tasklist.adapters.output.login.FindUserByEmailAdapter;
import br.com.jtech.tasklist.application.core.usecases.LoginUserUseCase;
import br.com.jtech.tasklist.config.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class LoginUserUseCaseConfig {

    @Bean
    public LoginUserUseCase loginUserUseCase(FindUserByEmailAdapter findUserByEmailAdapter,
                                              PasswordEncoder passwordEncoder,
                                              JwtTokenProvider jwtTokenProvider) {
        return new LoginUserUseCase(findUserByEmailAdapter, passwordEncoder, jwtTokenProvider);
    }
}
