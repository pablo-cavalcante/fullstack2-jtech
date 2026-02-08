package br.com.jtech.tasklist.application.core.usecases.task;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.core.usecases.Task.FindTasksByUserUseCase;
import br.com.jtech.tasklist.application.ports.output.task.FindTasksByUserOutputGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTasksByUserUseCaseTest {

    @Mock
    private FindTasksByUserOutputGateway findTasksByUserOutputGateway;

    private FindTasksByUserUseCase findTasksByUserUseCase;

    @BeforeEach
    void setUp() {
        findTasksByUserUseCase = new FindTasksByUserUseCase(findTasksByUserOutputGateway);
    }

    @Test
    @DisplayName("Should return tasks for a user")
    void shouldReturnTasksForUser() {
        var userId = "user-id-123";
        var tasks = List.of(
                Task.builder().id("1").title("Task 1").userId(userId).build(),
                Task.builder().id("2").title("Task 2").userId(userId).build()
        );

        when(findTasksByUserOutputGateway.findByUserId(userId)).thenReturn(tasks);

        var result = findTasksByUserUseCase.findByUserId(userId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Task 1");
        assertThat(result.get(1).getTitle()).isEqualTo("Task 2");
    }

    @Test
    @DisplayName("Should return empty list when user has no tasks")
    void shouldReturnEmptyListWhenNoTasks() {
        var userId = "user-id-123";
        when(findTasksByUserOutputGateway.findByUserId(userId)).thenReturn(Collections.emptyList());

        var result = findTasksByUserUseCase.findByUserId(userId);

        assertThat(result).isEmpty();
    }
}
