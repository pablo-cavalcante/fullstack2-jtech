package br.com.jtech.tasklist.application.ports.output.task;

import br.com.jtech.tasklist.application.core.domains.Task;

public interface CreateTaskOutputGateway {
    Task create(Task task);
}
