package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.ports.input.task.DeleteTaskInputGateway;
import br.com.jtech.tasklist.application.ports.output.task.DeleteTaskOutputGateway;
import br.com.jtech.tasklist.application.ports.output.task.FindTaskByIdOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.TaskNotFoundException;
import br.com.jtech.tasklist.config.infra.exceptions.UnauthorizedAccessException;

public class DeleteTaskUseCase implements DeleteTaskInputGateway {

    private final FindTaskByIdOutputGateway findTaskByIdOutputGateway;
    private final DeleteTaskOutputGateway deleteTaskOutputGateway;

    public DeleteTaskUseCase(FindTaskByIdOutputGateway findTaskByIdOutputGateway,
                             DeleteTaskOutputGateway deleteTaskOutputGateway) {
        this.findTaskByIdOutputGateway = findTaskByIdOutputGateway;
        this.deleteTaskOutputGateway = deleteTaskOutputGateway;
    }

    @Override
    public void delete(String taskId, String userId) {
        var task = findTaskByIdOutputGateway.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        if (!task.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to delete this task");
        }

        deleteTaskOutputGateway.delete(taskId);
    }
}
