package br.com.jtech.tasklist.application.core.usecases.login;

import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.core.usecases.Login.RegisterUserUseCase;
import br.com.jtech.tasklist.application.ports.output.login.RegisterUserOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private RegisterUserOutputGateway registerUserOutputGateway;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        registerUserUseCase = new RegisterUserUseCase(registerUserOutputGateway, passwordEncoder);
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() {
        var user = User.builder()
                .name("John Doe")
                .email("john@test.com")
                .password("password123")
                .build();

        var savedUser = User.builder()
                .id("user-id-123")
                .name("John Doe")
                .email("john@test.com")
                .password("encoded-password")
                .build();

        when(registerUserOutputGateway.existsByEmail("john@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(registerUserOutputGateway.save(any(User.class))).thenReturn(savedUser);

        var result = registerUserUseCase.register(user);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("user-id-123");
        assertThat(result.getEmail()).isEqualTo("john@test.com");
        verify(passwordEncoder).encode("password123");
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when email is already registered")
    void shouldThrowWhenEmailAlreadyExists() {
        var user = User.builder()
                .name("John")
                .email("existing@test.com")
                .password("password")
                .build();

        when(registerUserOutputGateway.existsByEmail("existing@test.com")).thenReturn(true);

        assertThatThrownBy(() -> registerUserUseCase.register(user))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("already exists");

        verify(registerUserOutputGateway, never()).save(any());
    }
}
