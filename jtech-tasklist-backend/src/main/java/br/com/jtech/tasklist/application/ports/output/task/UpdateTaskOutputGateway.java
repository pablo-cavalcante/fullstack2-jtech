package br.com.jtech.tasklist.application.ports.output.task;

import br.com.jtech.tasklist.application.core.domains.Task;

public interface UpdateTaskOutputGateway {
    Task update(Task task);
}
