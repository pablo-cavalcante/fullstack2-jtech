package br.com.jtech.tasklist.config.usecases.login;

import br.com.jtech.tasklist.application.core.usecases.RefreshTokenUseCase;
import br.com.jtech.tasklist.config.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RefreshTokenUseCaseConfig {

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(JwtTokenProvider jwtTokenProvider) {
        return new RefreshTokenUseCase(jwtTokenProvider);
    }
}
