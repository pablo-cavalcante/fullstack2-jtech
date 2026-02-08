package br.com.jtech.tasklist.application.core.usecases.task;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.core.usecases.Task.UpdateTaskUseCase;
import br.com.jtech.tasklist.application.ports.output.task.FindTaskByIdOutputGateway;
import br.com.jtech.tasklist.application.ports.output.task.UpdateTaskOutputGateway;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateTaskUseCaseTest {

    @Mock
    private FindTaskByIdOutputGateway findTaskByIdOutputGateway;

    @Mock
    private UpdateTaskOutputGateway updateTaskOutputGateway;

    private UpdateTaskUseCase updateTaskUseCase;

    @BeforeEach
    void setUp() {
        updateTaskUseCase = new UpdateTaskUseCase(findTaskByIdOutputGateway, updateTaskOutputGateway);
    }

    @Test
    @DisplayName("Should update task successfully")
    void shouldUpdateTaskSuccessfully() {
        var taskId = "task-id-123";
        var userId = "user-id-456";
        var existingTask = Task.builder().id(taskId).title("Old Title").userId(userId).completed(false).build();
        var updateData = Task.builder().title("New Title").description("New Desc").completed(true).build();
        var updatedTask = Task.builder().id(taskId).title("New Title").description("New Desc").completed(true).userId(userId).build();

        when(findTaskByIdOutputGateway.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(updateTaskOutputGateway.update(any(Task.class))).thenReturn(updatedTask);

        var result = updateTaskUseCase.update(taskId, updateData, userId);

        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getDescription()).isEqualTo("New Desc");
        assertThat(result.getCompleted()).isTrue();
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when task does not exist")
    void shouldThrowWhenTaskNotFound() {
        when(findTaskByIdOutputGateway.findById("non-existent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateTaskUseCase.update("non-existent", Task.builder().build(), "user"))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw UnauthorizedAccessException when user is not the owner")
    void shouldThrowWhenNotOwner() {
        var taskId = "task-id";
        var existingTask = Task.builder().id(taskId).userId("other-user").build();

        when(findTaskByIdOutputGateway.findById(taskId)).thenReturn(Optional.of(existingTask));

        assertThatThrownBy(() -> updateTaskUseCase.update(taskId, Task.builder().build(), "my-user"))
                .isInstanceOf(UnauthorizedAccessException.class);
    }
}
