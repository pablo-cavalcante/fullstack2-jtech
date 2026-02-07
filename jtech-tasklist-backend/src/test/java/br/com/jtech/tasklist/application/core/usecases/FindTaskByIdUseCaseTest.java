package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.output.task.FindTaskByIdOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.TaskNotFoundException;
import br.com.jtech.tasklist.config.infra.exceptions.UnauthorizedAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTaskByIdUseCaseTest {

    @Mock
    private FindTaskByIdOutputGateway findTaskByIdOutputGateway;

    private FindTaskByIdUseCase findTaskByIdUseCase;

    @BeforeEach
    void setUp() {
        findTaskByIdUseCase = new FindTaskByIdUseCase(findTaskByIdOutputGateway);
    }

    @Test
    @DisplayName("Should find task by id successfully")
    void shouldFindTaskByIdSuccessfully() {
        var taskId = "task-id-123";
        var userId = "user-id-456";
        var task = Task.builder().id(taskId).title("Test").userId(userId).build();

        when(findTaskByIdOutputGateway.findById(taskId)).thenReturn(Optional.of(task));

        var result = findTaskByIdUseCase.findById(taskId, userId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(taskId);
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when task does not exist")
    void shouldThrowWhenTaskNotFound() {
        var taskId = "non-existent";
        var userId = "user-id";

        when(findTaskByIdOutputGateway.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> findTaskByIdUseCase.findById(taskId, userId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    @DisplayName("Should throw UnauthorizedAccessException when task belongs to another user")
    void shouldThrowWhenTaskBelongsToAnotherUser() {
        var taskId = "task-id-123";
        var userId = "user-id-456";
        var task = Task.builder().id(taskId).title("Test").userId("other-user").build();

        when(findTaskByIdOutputGateway.findById(taskId)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> findTaskByIdUseCase.findById(taskId, userId))
                .isInstanceOf(UnauthorizedAccessException.class)
                .hasMessageContaining("permission");
    }
}
