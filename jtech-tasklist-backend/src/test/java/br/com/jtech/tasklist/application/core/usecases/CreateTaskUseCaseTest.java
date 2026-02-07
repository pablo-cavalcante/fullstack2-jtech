package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.output.task.CreateTaskOutputGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTaskUseCaseTest {

    @Mock
    private CreateTaskOutputGateway createTaskOutputGateway;

    private CreateTaskUseCase createTaskUseCase;

    @BeforeEach
    void setUp() {
        createTaskUseCase = new CreateTaskUseCase(createTaskOutputGateway);
    }

    @Test
    @DisplayName("Should create a task successfully")
    void shouldCreateTaskSuccessfully() {
        var task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .userId("user-id-123")
                .build();

        var savedTask = Task.builder()
                .id("task-id-456")
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .userId("user-id-123")
                .build();

        when(createTaskOutputGateway.create(any(Task.class))).thenReturn(savedTask);

        var result = createTaskUseCase.create(task);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("task-id-456");
        assertThat(result.getTitle()).isEqualTo("Test Task");
        assertThat(result.getCompleted()).isFalse();
        verify(createTaskOutputGateway, times(1)).create(any(Task.class));
    }

    @Test
    @DisplayName("Should set completed to false when creating task")
    void shouldSetCompletedToFalseWhenCreating() {
        var task = Task.builder()
                .title("Task")
                .completed(true)
                .build();

        when(createTaskOutputGateway.create(any(Task.class))).thenReturn(task);

        createTaskUseCase.create(task);

        assertThat(task.getCompleted()).isFalse();
    }
}
