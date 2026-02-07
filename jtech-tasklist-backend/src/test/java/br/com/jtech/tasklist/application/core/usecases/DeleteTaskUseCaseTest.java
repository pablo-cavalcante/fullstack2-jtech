package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.output.task.DeleteTaskOutputGateway;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTaskUseCaseTest {

    @Mock
    private FindTaskByIdOutputGateway findTaskByIdOutputGateway;

    @Mock
    private DeleteTaskOutputGateway deleteTaskOutputGateway;

    private DeleteTaskUseCase deleteTaskUseCase;

    @BeforeEach
    void setUp() {
        deleteTaskUseCase = new DeleteTaskUseCase(findTaskByIdOutputGateway, deleteTaskOutputGateway);
    }

    @Test
    @DisplayName("Should delete task successfully")
    void shouldDeleteTaskSuccessfully() {
        var taskId = "task-id-123";
        var userId = "user-id-456";
        var task = Task.builder().id(taskId).userId(userId).build();

        when(findTaskByIdOutputGateway.findById(taskId)).thenReturn(Optional.of(task));

        deleteTaskUseCase.delete(taskId, userId);

        verify(deleteTaskOutputGateway, times(1)).delete(taskId);
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when task does not exist")
    void shouldThrowWhenTaskNotFound() {
        when(findTaskByIdOutputGateway.findById("non-existent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteTaskUseCase.delete("non-existent", "user"))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw UnauthorizedAccessException when user is not the owner")
    void shouldThrowWhenNotOwner() {
        var taskId = "task-id";
        var task = Task.builder().id(taskId).userId("other-user").build();

        when(findTaskByIdOutputGateway.findById(taskId)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> deleteTaskUseCase.delete(taskId, "my-user"))
                .isInstanceOf(UnauthorizedAccessException.class);

        verify(deleteTaskOutputGateway, never()).delete(any());
    }
}
