package br.com.jtech.tasklist.application.core.usecases.Task;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.input.task.FindTaskByIdInputGateway;
import br.com.jtech.tasklist.application.ports.output.task.FindTaskByIdOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.TaskNotFoundException;
import br.com.jtech.tasklist.config.infra.exceptions.UnauthorizedAccessException;

public class FindTaskByIdUseCase implements FindTaskByIdInputGateway {

    private final FindTaskByIdOutputGateway findTaskByIdOutputGateway;

    public FindTaskByIdUseCase(FindTaskByIdOutputGateway findTaskByIdOutputGateway) {
        this.findTaskByIdOutputGateway = findTaskByIdOutputGateway;
    }

    @Override
    public Task findById(String taskId, String userId) {
        var task = findTaskByIdOutputGateway.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        if (!task.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to access this task");
        }

        return task;
    }
}
