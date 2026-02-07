package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.login.FindUserByEmailOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.InvalidCredentialsException;
import br.com.jtech.tasklist.config.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock
    private FindUserByEmailOutputGateway findUserByEmailOutputGateway;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private LoginUserUseCase loginUserUseCase;

    @BeforeEach
    void setUp() {
        loginUserUseCase = new LoginUserUseCase(findUserByEmailOutputGateway, passwordEncoder, jwtTokenProvider);
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void shouldLoginSuccessfully() {
        var email = "john@test.com";
        var password = "password123";
        var user = User.builder()
                .id("user-id-123")
                .email(email)
                .password("encoded-password")
                .build();

        when(findUserByEmailOutputGateway.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encoded-password")).thenReturn(true);
        when(jwtTokenProvider.generateToken("user-id-123", email)).thenReturn("access-token");
        when(jwtTokenProvider.generateRefreshToken("user-id-123", email)).thenReturn("refresh-token");

        var result = loginUserUseCase.login(email, password);

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo("access-token");
        assertThat(result.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(result.getType()).isEqualTo("Bearer");
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when email not found")
    void shouldThrowWhenEmailNotFound() {
        when(findUserByEmailOutputGateway.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginUserUseCase.login("unknown@test.com", "password"))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("Invalid email or password");
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when password is wrong")
    void shouldThrowWhenPasswordIsWrong() {
        var user = User.builder()
                .id("user-id")
                .email("john@test.com")
                .password("encoded-password")
                .build();

        when(findUserByEmailOutputGateway.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        assertThatThrownBy(() -> loginUserUseCase.login("john@test.com", "wrong-password"))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
