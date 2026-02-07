package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.input.task.CreateTaskInputGateway;
import br.com.jtech.tasklist.application.ports.output.task.CreateTaskOutputGateway;

public class CreateTaskUseCase implements CreateTaskInputGateway {

    private final CreateTaskOutputGateway createTaskOutputGateway;

    public CreateTaskUseCase(CreateTaskOutputGateway createTaskOutputGateway) {
        this.createTaskOutputGateway = createTaskOutputGateway;
    }

    @Override
    public Task create(Task task) {
        task.setCompleted(false);
        return createTaskOutputGateway.create(task);
    }
}
