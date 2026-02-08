package br.com.jtech.tasklist.application.core.usecases.Task;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.input.task.UpdateTaskInputGateway;
import br.com.jtech.tasklist.application.ports.output.task.FindTaskByIdOutputGateway;
import br.com.jtech.tasklist.application.ports.output.task.UpdateTaskOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.TaskNotFoundException;
import br.com.jtech.tasklist.config.infra.exceptions.UnauthorizedAccessException;

public class UpdateTaskUseCase implements UpdateTaskInputGateway {

    private final FindTaskByIdOutputGateway findTaskByIdOutputGateway;
    private final UpdateTaskOutputGateway updateTaskOutputGateway;

    public UpdateTaskUseCase(FindTaskByIdOutputGateway findTaskByIdOutputGateway,
                             UpdateTaskOutputGateway updateTaskOutputGateway) {
        this.findTaskByIdOutputGateway = findTaskByIdOutputGateway;
        this.updateTaskOutputGateway = updateTaskOutputGateway;
    }

    @Override
    public Task update(String taskId, Task task, String userId) {
        var existingTask = findTaskByIdOutputGateway.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        if (!existingTask.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to update this task");
        }

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        if (task.getCompleted() != null) {
            existingTask.setCompleted(task.getCompleted());
        }

        return updateTaskOutputGateway.update(existingTask);
    }
}
